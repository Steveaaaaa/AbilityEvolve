package yezi.abilityevolve.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.config.SkillLockLoader;

import java.util.List;

public class Tooltips {
  //  private static final Logger LOGGER = Logger.getLogger(Tooltips.class.getName());

    public Tooltips() {}

    @SubscribeEvent
    public void onTooltipDisplay(ItemTooltipEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.isCreative()) return;

        ItemStack stack = event.getItemStack();
        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (itemRegistryName == null) return;

        Requirement[] useRequirements = SkillLockLoader.getRequirements(itemRegistryName);
        Requirement[] craftRequirements = SkillLockLoader.getCraftRequirements(itemRegistryName);
        Requirement[] attackRequirements = SkillLockLoader.getAttackRequirements(itemRegistryName);

        if (useRequirements == null && craftRequirements == null && attackRequirements == null) return;

        SkillModel skillModel = ModCapabilities.getSkillModel(player);
        List<Component> tooltips = event.getToolTip();
        tooltips.add(Component.literal(""));
        tooltips.add(Component.translatable("tooltip.requirements").append(":").withStyle(ChatFormatting.WHITE));
        addRequirementsTooltip(tooltips, useRequirements, skillModel);
        addRequirementsTooltip(tooltips, craftRequirements, skillModel);
        addRequirementsTooltip(tooltips, attackRequirements, skillModel);
    }

    private void addRequirementsTooltip(List<Component> tooltips, Requirement[] requirements, SkillModel skillModel) {
        if (requirements == null) return;

        for (Requirement requirement : requirements) {
            int skillLevel = skillModel.getSkillLevel(requirement.index);
            ChatFormatting color = skillLevel >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
            tooltips.add(
                    Component.translatable(Skill.fromIndex(requirement.index).getDisplayName())
                            .append(" " + requirement.level)
                            .withStyle(color)
            );
        }
    }
}
