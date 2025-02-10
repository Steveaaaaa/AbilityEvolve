package yezi.abilityevolve.common.skills;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import yezi.abilityevolve.common.capabilities.ModCapabilities;

public class SkillRequirementChecker {
   // private static SkillModel skillModel;

    public SkillRequirementChecker() {
      //  SkillRequirementChecker.skillModel = skillModel;
    }

    public static boolean canUseItem(Player player, ItemStack item) {
        return canUse(player, item.getItem().builtInRegistryHolder().key().location(), RequirementType.USE);
    }

    public static boolean canUseBlock(Player player, Block block) {
        return canUse(player, block.builtInRegistryHolder().key().location(), RequirementType.USE);
    }

    public static boolean canUseEntity(Player player, Entity entity) {
        return canUse(player, entity.getType().builtInRegistryHolder().key().location(), RequirementType.USE);
    }

    public static boolean canCraftItem(Player player, ItemStack stack) {
        return canUse(player, stack.getItem().builtInRegistryHolder().key().location(), RequirementType.CRAFT);
    }

    public static boolean canAttackEntity(Player player, Entity target) {
        return canUse(player, target.getType().builtInRegistryHolder().key().location(), RequirementType.ATTACK);
    }

    private static boolean canUse(Player player, ResourceLocation resource, RequirementType type) {
        Requirement[] requirements = type.getRequirements(resource);
        if (requirements != null) {
            for (Requirement requirement : requirements) {
                if (ModCapabilities.getSkillModel(player).getSkillLevel(requirement.index) < requirement.level) {
                    sendFailureMessage(player, type);
                    return false;
                }
            }
        }
        return true;
    }

    private static void sendFailureMessage(Player player, RequirementType type) {
        if (player instanceof ServerPlayer) {
            String message = switch (type) {
                case ATTACK -> "You are not strong enough to attack this creature.";
                default -> "You are not skilled enough to use this item.";
            };
            player.sendSystemMessage(Component.literal(message));
        }
    }
}
