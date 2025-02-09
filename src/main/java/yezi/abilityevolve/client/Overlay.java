package yezi.abilityevolve.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.config.ConfigManager;
import yezi.abilityevolve.client.screen.SkillScreen;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.RequirementType;

import java.util.List;
import java.util.Locale;

public class Overlay implements IGuiOverlay {
    private static List<Requirement> requirements = List.of();
    private static int showTicks = 0;
    private static String messageKey = "";

    public Overlay() {}

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        showTicks = Math.max(0, showTicks - 1);
    }

    public static void showWarning(ResourceLocation resource, RequirementType type) {
        requirements = List.of(type.getRequirements(resource));
        messageKey = "overlay.message." + type.name().toLowerCase(Locale.ROOT);
        showTicks = 60;
    }

    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (showTicks <= 0) return;

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || !player.getCapability(ModCapabilities.SKILL_MODEL_CAPABILITY).isPresent()) return;

        int centerX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int centerY = minecraft.getWindow().getGuiScaledHeight() / 4;

        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
        RenderSystem.enableBlend();
        guiGraphics.blit(SkillScreen.RESOURCES, centerX - 71, centerY - 4, 0, 194, 142, 40);

        String message = Component.translatable(messageKey).getString();
        guiGraphics.drawString(minecraft.font, message, centerX - minecraft.font.width(message) / 2, centerY, 0xFFCC55, false);

        if (requirements.isEmpty()) return;

        SkillModel skillModel = ClientUtils.getClientSkillModel();
        int maxLevel = ConfigManager.getMaxLevel();

        for (int i = 0; i < requirements.size(); i++) {
            Requirement requirement = requirements.get(i);
            int skillLevel = skillModel.getSkillLevel(requirement.index);
            boolean met = skillLevel >= requirement.level;

            int x = centerX + i * 20 - requirements.size() * 10 + 2;
            int y = centerY + 15;
            int u = Math.min(requirement.level, maxLevel - 1) / (maxLevel / 4) * 16 + 176;
            int v = requirement.index * 16 + 128;

            guiGraphics.blit(SkillScreen.RESOURCES, x, y, u, v, 16, 16);

            String levelText = Integer.toString(requirement.level);
            guiGraphics.drawString(minecraft.font, levelText, x + 17 - minecraft.font.width(levelText), y + 9, met ? 0x7EFC20 : 0xFC5454, false);
        }
    }
}

