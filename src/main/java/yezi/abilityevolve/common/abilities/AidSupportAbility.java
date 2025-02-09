package yezi.abilityevolve.common.abilities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AidSupportAbility extends Ability {
    private static final String name = "aid_support";
    private static final String description = "Enhance the nearby corresponding fed creatures.";
    private static final int requirementGraziery = 12;
    public AidSupportAbility() {
        super(
                name,
                description,
                new Requirement[]{new Requirement(Skill.GRAZIERY, requirementGraziery),new Requirement(Skill.FARMING,8)},
                "graziery", 0, 6, true
        );
    }

    private static final int BASE_RADIUS = 12;
  //  private static final double BASE_HEAL_PERCENT = 0.06; // 6% 的最大生命值
    private static final int DURATION_TICKS = 599; // 29.95秒
    private static final int HEAL_INTERVAL = 100; // 每 5s 触发一次 (20 ticks = 1s)
    private static final double TOTAL_HEAL_PERCENT = 0.20; // 持续期间总共恢复 20%

    GetAbilityLevel getAbilityLevel = new GetAbilityLevel();
    int skillLevel = SkillModel.get().getSkillLevel(Skill.GRAZIERY);
    int abilityLevel = getAbilityLevel.getAbilityLevelGraziery1(skillLevel, requirementGraziery);
    private static final Map<Item, List<EntityType<?>>> FEEDING_MAP = Map.of(
            Items.SNOW_BLOCK, List.of(EntityType.SNOW_GOLEM),
            Items.IRON_INGOT, List.of(EntityType.IRON_GOLEM),
            Items.RABBIT_STEW, List.of(EntityType.WOLF),
            Items.GOLDEN_APPLE, List.of(EntityType.HORSE, EntityType.DONKEY,EntityType.MULE),
            Items.HAY_BLOCK, List.of(EntityType.LLAMA),
            Items.GOLDEN_CARROT, List.of(EntityType.PIG)
    );
    public void onItemUse(Player player, ItemStack itemStack) {
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
            Objects.requireNonNull(level.getServer()).submitAsync(() -> {
                while (remainingTime > 0) {
                    try {
                        Thread.sleep(interval * 50L);
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
