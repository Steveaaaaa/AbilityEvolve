package yezi.abilityevolve.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import yezi.abilityevolve.client.screen.buttons.SkillButton;
import yezi.abilityevolve.common.skills.Skill;

public class SkillScreen extends Screen {
    public static final ResourceLocation RESOURCES = new ResourceLocation("AbilityEvolve", "textures/gui/skills.png");
    private final int centerX;
    private final int centerY;
    private final Skill[] skills = Skill.values(); // 预存所有技能

    public SkillScreen() {
        super(Component.translatable("container.skills")
                .setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.BLACK)));

        // 预计算界面中心点
        this.centerX = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 176) / 2;
        this.centerY = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - 166) / 2;
    }

    @Override
    protected void init() {
        for (int i = 0; i < skills.length; i++) {
            int x = centerX + (i % 2) * 83;
            int y = centerY + (i / 2) * 36;
            this.addRenderableWidget(new SkillButton(x, y, skills[i]));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        RenderSystem.setShaderTexture(0, RESOURCES);
        guiGraphics.blit(RESOURCES, centerX, centerY, 0, 0, 176, 166);

        // 渲染标题
        String titleText = this.title.getString();
        guiGraphics.drawString(this.font, titleText, centerX + (176 - this.font.width(titleText)) / 2, centerY + 6, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

