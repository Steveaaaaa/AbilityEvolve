package yezi.abilityevolve.common.abilities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.listener.AidSupportListener;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.List;
import java.util.Map;

public class AidSupportAbility extends Ability {
    public static final String name = "aid_support";
    public static final String description = "Enhance the nearby corresponding fed creatures.";
    public static final int requirementGraziery = 12;

    public AidSupportAbility() {
        super(
                name,
                description,
                new Requirement[]{new Requirement(Skill.GRAZIERY.index, requirementGraziery), new Requirement(Skill.FARMING.index, 8)},
                "graziery", 0, 6, true
        );
    }

    private static final int BASE_RADIUS = 12;
    public static final int DURATION_TICKS = 600;

    public static final int HEAL_INTERVAL = 100;
    private static final double TOTAL_HEAL_PERCENT = 0.20;

    private static final Map<Item, List<EntityType<?>>> FEEDING_MAP = Map.of(
            Items.SNOW_BLOCK, List.of(EntityType.SNOW_GOLEM),
            Items.IRON_INGOT, List.of(EntityType.IRON_GOLEM),
            Items.RABBIT_STEW, List.of(EntityType.WOLF),
            Items.GOLDEN_APPLE, List.of(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE),
            Items.HAY_BLOCK, List.of(EntityType.LLAMA),
            Items.GOLDEN_CARROT, List.of(EntityType.PIG)
    );

    public static void onItemUse(Player player, ItemStack itemStack) {
        if (!FEEDING_MAP.containsKey(itemStack.getItem())) return;

        int skillLevel = ModCapabilities.getSkillModel(player).getSkillLevel(Skill.GRAZIERY);
        int abilityLevel = GetAbilityLevel.getAbilityLevelGraziery1(skillLevel, requirementGraziery);
        double HEAL_PERCENTAGE = 0.5 + abilityLevel * 0.1;

        List<EntityType<?>> targetType = FEEDING_MAP.get(itemStack.getItem());
        Level level = player.level();

        if (!(level instanceof ServerLevel serverLevel)) return;

        List<LivingEntity> entities = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(BASE_RADIUS),
                e -> targetType.contains(e.getType())
        );

        double totalHeal = player.getMaxHealth() * HEAL_PERCENTAGE;
        double healPerInterval = totalHeal * TOTAL_HEAL_PERCENT;

        for (LivingEntity entity : entities) {
            AidSupportListener.addHealingTask(entity, (float) healPerInterval);
        }
    }
}


