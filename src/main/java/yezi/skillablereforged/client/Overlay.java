package yezi.skillablereforged.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.client.screen.SkillScreen;
import yezi.skillablereforged.common.capabilities.SkillCapability;
import yezi.skillablereforged.common.commands.skills.Requirement;
import yezi.skillablereforged.common.commands.skills.RequirementType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Overlay implements IGuiOverlay {
    private static List<Requirement> requirements = null;
    private static int showTicks = 0;
    private static String messageKey = "";

    public Overlay() {
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (showTicks > 0) {
            --showTicks;
        }

    }

    public static void showWarning(ResourceLocation resource, RequirementType type) {
        requirements = Arrays.asList(type.getRequirements(resource));
        String var10000 = type.name();
        messageKey = "overlay.message." + var10000.toLowerCase(Locale.ROOT);
        showTicks = 60;
    }

    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        if (showTicks > 0 && minecraft.player != null && minecraft.player.getCapability(SkillCapability.INSTANCE).isPresent()) {
            PoseStack stack = guiGraphics.pose();
            RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
            RenderSystem.enableBlend();
            int cx = minecraft.getWindow().getGuiScaledWidth() / 2;
            int cy = minecraft.getWindow().getGuiScaledHeight() / 4;
            guiGraphics.blit(SkillScreen.RESOURCES, cx - 71, cy - 4, 0, 194, 142, 40);
            String message = Component.literal(messageKey).getString();
            guiGraphics.drawString(minecraft.font, message, cx - minecraft.font.width(message) / 2, cy, 16733525, false);

            for(int i = 0; i < requirements.size(); ++i) {
                Requirement requirement = (Requirement)requirements.get(i);
                int maxLevel = Config.getMaxLevel();
                int x = cx + i * 20 - requirements.size() * 10 + 2;
                int y = cy + 15;
                int u = Math.min(requirement.level, maxLevel - 1) / (maxLevel / 4) * 16 + 176;
                int v = requirement.skill.index * 16 + 128;
                RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
                guiGraphics.blit(SkillScreen.RESOURCES, x, y, u, v, 16, 16);
                String level = Integer.toString(requirement.level);
                boolean met = ClientUtils.getClientSkillModel().getSkillLevel(requirement.skill) >= requirement.level;
                guiGraphics.drawString(minecraft.font, level, x + 17 - minecraft.font.width(level), y + 9, met ? 0x7EFC20 : 0xFC5454, false);
            }
        }
    }
}
