package yezi.skillablereforged.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.Style;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.client.screen.buttons.SkillButton;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Skill;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SkillScreen extends Screen {
    public static final ResourceLocation RESOURCES = new ResourceLocation("skillablereforged", "textures/gui/skills.png");

    public SkillScreen() {
        super(Component.translatable("container.skills")
                .setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.BLACK)));
    }
    @Override
    protected void init() {
        // 计算 GUI 左上角的位置，确保按钮布局居中
        int left = (this.width - 162) / 2;
        int top = (this.height - 128) / 2;

        // 创建 8 个按钮
        for (int i = 0; i < 8; ++i) {
            int x = left + (i % 2) * 83; // 按钮在 x 轴的位置
            int y = top + (i / 2) * 36;  // 按钮在 y 轴的位置

            // 获取技能的枚举值，假设 Skill.values() 是一个包含所有技能的枚举
            Skill skill = Skill.values()[i];

            // 创建一个 SkillButton 按钮并添加到界面
            this.addRenderableWidget(new SkillButton(x, y, skill));
        }
    }

    private int getLevel(Skill skill) {
        if (Minecraft.getInstance().player!= null) {
            SkillModel skillModel = SkillModel.get(Minecraft.getInstance().player);
            return skillModel.getSkillLevel(skill);
        } else {
            return 0;
        }
    }

    private int getMaxLevel() {
        return Config.getMaxLevel();
    }
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, RESOURCES);
        int left = (this.width - 176) / 2;
        int top = (this.height - 166) / 2;

        this.renderBackground(guiGraphics);
        guiGraphics.blit(RESOURCES, left, top, 0, 0, 176, 166);
        guiGraphics.drawString(this.font, this.title.getString(), this.width / 2 - this.font.width(this.title) / 2, top + 6, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
