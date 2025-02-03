package yezi.skillablereforged.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.commands.skills.Requirement;

import java.util.List;
import java.util.logging.Logger;

public class Tooltips {
    private static final Logger LOGGER = Logger.getLogger(Tooltips.class.getName());

    public Tooltips() {
    }

    @SubscribeEvent
    public void onTooltipDisplay(ItemTooltipEvent event) {
        if (Minecraft.getInstance().player != null && !Minecraft.getInstance().player.isCreative()) {
            ItemStack stack = event.getItemStack();
            ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (itemRegistryName != null) {
                Requirement[] requirements = Config.getRequirements(itemRegistryName);
                Requirement[] requirementsCraft = Config.getCraftRequirements(itemRegistryName);
                Requirement[] requirementsAttack = Config.getEntityAttackRequirements(itemRegistryName);
              //  LOGGER.info("Retrieved requirements for " + itemRegistryName + ": " + Arrays.toString(requirements));
                if (requirements != null || requirementsCraft != null || requirementsAttack != null) {
                    List<Component> tooltips = event.getToolTip();
                    tooltips.add(Component.literal(""));
                    tooltips.add(Component.translatable("tooltip.requirements")
                            .append(":")
                            .withStyle(ChatFormatting.GRAY));
                    SkillModel skillModel = SkillModel.get(Minecraft.getInstance().player);
                        if (requirements != null) {
                            for (Requirement requirement : requirements) {
                                ChatFormatting colour = skillModel.getSkillLevel(requirement.skill) >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
                                tooltips.add(
                                        Component.translatable(requirement.skill.displayName)
                                                .append(" " + requirement.level)
                                                .withStyle(colour));
                            }
                        }
                        if (requirementsCraft != null) {
                            for (Requirement requirement : requirementsCraft) {
                                ChatFormatting colour = skillModel.getSkillLevel(requirement.skill) >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
                                tooltips.add(
                                        Component.translatable(requirement.skill.displayName)
                                                .append(" " + requirement.level)
                                                .withStyle(colour));
                            }
                        }
                        if (requirementsAttack != null) {
                            for (Requirement requirement : requirementsAttack) {
                                ChatFormatting colour = skillModel.getSkillLevel(requirement.skill) >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
                                tooltips.add(
                                        Component.translatable(requirement.skill.displayName)
                                                .append(" " + requirement.level)
                                                .withStyle(colour));
                            }
                        }
                    }
                }
            }
        }
    }