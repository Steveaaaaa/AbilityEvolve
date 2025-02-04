package yezi.skillablereforged.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.client.screen.SkillScreen;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.network.RequestLevelUp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class SkillButton extends Button {
    private final Skill skill;

    public SkillButton(int x, int y, Skill skill) {
        // 使用 Button.Builder 创建按钮
        super(
                x, y, 79, 32,
                Component.empty(),    
                onPress -> {
                    RequestLevelUp.send(skill);
                },
                Button.DEFAULT_NARRATION
        );
        this.skill = skill;
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

        assert minecraft.player != null;
        SkillModel skillModel = SkillModel.get(minecraft.player);
        int level = skillModel.getSkillLevel(this.skill);
        int maxLevel = Config.getMaxLevel();

        int u = ((int) Math.ceil((double) level * 4 / maxLevel) - 1) * 16 + 176;
        int v = skill.index * 16 + 128;

        // 绘制技能按钮背景
        guiGraphics.blit(SkillScreen.RESOURCES, this.getX(), this.getY(),
                176, (level == maxLevel ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0),
                this.width, this.height);

        guiGraphics.blit(SkillScreen.RESOURCES, this.getX() + 6, this.getY() + 8, u, v, 16, 16);
       // guiGraphics.pose().pushPose();

     //   PoseStack poseStack = guiGraphics.pose();
       // MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        Font font = minecraft.font;
        String skillName = this.skill.getDisplayName();
        guiGraphics.drawString(font,Component.translatable(skillName),this.getX()+25,this.getY()+7,0xFFFFFF);
        guiGraphics.drawString(font,Component.translatable(level + "/" + maxLevel),this.getX()+25,this.getY()+18,0xFFFFFF);
       /* font.drawInBatch(Component.literal(skillName),
                (float)(this.getX() + 25),
                (float)(this.getY() + 7),
                0xFFFFFF,
                false,
                poseStack.last().pose(),
                bufferSource,
                DisplayMode.NORMAL,
                0,
                0x555555);

        font.drawInBatch(Component.literal(level + "/" + maxLevel),
                (float)(this.getX() + 25),
                (float)(this.getY() + 18),
                0xFFFFFF,
                false,
                poseStack.last().pose(),
                bufferSource,
                DisplayMode.NORMAL,
                0,
                0x555555);*/

      //  bufferSource.endBatch();

        // 绘制经验值和升级条件
        if (isMouseOver(mouseX, mouseY) && level < maxLevel) {
            int cost = Config.getStartCost() + (level - 1) * Config.getCostIncrease();
            assert minecraft.player != null;
            int color = minecraft.player.experienceLevel >= cost ? 0x7EFC20 : 0xFC5454;
            String costText = Integer.toString(cost);
            int textWidth = font.width(costText);
            guiGraphics.drawString(font, costText, this.getX() + 73 - textWidth, this.getY() + 18, color);
        }
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
}
