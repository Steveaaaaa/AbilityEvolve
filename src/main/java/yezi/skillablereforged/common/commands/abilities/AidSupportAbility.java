package yezi.skillablereforged.common.commands.abilities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.commands.skills.Requirement;
import yezi.skillablereforged.common.commands.skills.RequirementType;
import yezi.skillablereforged.common.commands.skills.Skill;

import java.util.List;
import java.util.Map;

public class AidSupportAbility extends Ability {
    private static final Requirement[] CACHED_REQUIREMENTS = loadRequirements();
    public AidSupportAbility() {
        super(
                "aid_support",
                "使用物品强化周围生物",
                CACHED_REQUIREMENTS,
                0, 0, 6, true
        );
    }

    private static final int BASE_RADIUS = 12;
  //  private static final double BASE_HEAL_PERCENT = 0.06; // 6% 的最大生命值
    private static final int DURATION_TICKS = 599; // 29.95秒
    private static final int HEAL_INTERVAL = 100; // 每 5s 触发一次 (20 ticks = 1s)
    private static final double TOTAL_HEAL_PERCENT = 0.20; // 持续期间总共恢复 20%

    SkillModel skillModel = SkillModel.get();
    Skill skill = Skill.values()[abilityType];
    private static final Map<Item, List<EntityType<?>>> FEEDING_MAP = Map.of(
            Items.SNOW_BLOCK, List.of(EntityType.SNOW_GOLEM),
            Items.IRON_INGOT, List.of(EntityType.IRON_GOLEM),
            Items.RABBIT_STEW, List.of(EntityType.WOLF),
            Items.GOLDEN_APPLE, List.of(EntityType.HORSE, EntityType.DONKEY,EntityType.MULE),
            Items.HAY_BLOCK, List.of(EntityType.LLAMA),
            Items.GOLDEN_CARROT, List.of(EntityType.PIG)
    );

 //   private static final int requirement = 12;

    private static Requirement[] loadRequirements() {
       // Requirement[] abilityRequirements = RequirementType.ABILITY.getRequirements("aid_support");
        return RequirementType.ABILITY.getRequirements("aid_support");
    }

    @Override
    public void onItemUse(Player player, ItemStack itemStack) {
        int requiredLevel = 0;
        for (Requirement req : CACHED_REQUIREMENTS) {
            if (req.skill == Skill.GRAZIERY) { // 查找 graziery 对应的要求
                requiredLevel = req.level;
                break;
            }
        }
        if (skillModel.getSkillLevel(skill) >= requiredLevel) {
            abilityLevel = skillModel.getSkillLevel(skill) - requiredLevel;
        }
        double HEAL_PERCENTAGE = 0.5 + abilityLevel * 0.1;
        if (!FEEDING_MAP.containsKey(itemStack.getItem())) return;
        List<EntityType<?>> targetType = FEEDING_MAP.get(itemStack.getItem());
        Level level = player.level();
        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(BASE_RADIUS),
                e -> targetType.contains(e.getType())
        );

        double totalHeal = player.getMaxHealth() * HEAL_PERCENTAGE;
        double healPerInterval = totalHeal * TOTAL_HEAL_PERCENT;

        for (LivingEntity entity : entities) {
            new TimerTask(level, entity, (float) healPerInterval).start(DURATION_TICKS, HEAL_INTERVAL);
        }
    }

    private static class TimerTask {
        private final Level level;
        private final LivingEntity entity;
        private final float healAmount;
        private int remainingTime;

        public TimerTask(Level level, LivingEntity entity, float healAmount) {
            this.level = level;
            this.entity = entity;
            this.healAmount = healAmount;
            this.remainingTime = DURATION_TICKS;
        }

        public void start(int duration, int interval) {
            this.remainingTime = duration;
            level.getServer().submitAsync(() -> {
                while (remainingTime > 0) {
                    try {
                        Thread.sleep(interval * 50L); // Convert ticks to milliseconds
                    } catch (InterruptedException e) {
                        return;
                    }
                    if (entity.isAlive()) {
                        entity.heal(healAmount);
                    }
                    remainingTime -= interval;
                }
            });
        }
    }
}

   /* public void activate(Player player, ItemStack usedItem) {
        if (skillModel.getSkillLevel(skill) >= requirement){
            abilityLevel = skillModel.getSkillLevel(skill) - requirement;
        }
        Level world = player.level();
        AABB range = new AABB(player.blockPosition()).inflate(BASE_RADIUS);
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, range);

        for (LivingEntity entity : entities) {
            double healAmount = entity.getMaxHealth() * (BASE_HEAL_PERCENT + abilityLevel * 0.01); // 每级增加 1% 回复
            entity.heal((float) healAmount);
        }
        if (usedItem.getItem() == Items.GOLDEN_CARROT || usedItem.getItem() == Items.GOLDEN_APPLE) {
            for (LivingEntity entity : entities) {
                if (entity instanceof Pig || entity instanceof Horse || entity instanceof Donkey) {
                    entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 500, 50));
                }
            }
        }*/
