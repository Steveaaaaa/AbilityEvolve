package yezi.abilityevolve.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import yezi.abilityevolve.Config;
import yezi.abilityevolve.client.screen.SkillScreen;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.network.RequestLevelUp;
import yezi.abilityevolve.common.skills.Skill;

public class SkillButton extends Button {
    private final Skill skill;

    public SkillButton(int x, int y, Skill skill) {
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

        int u = ((int) Math.ceil((double) ModCapabilities.getSkillModel(minecraft.player).getSkillLevel(this.skill.index) * 4 / Config.getMaxLevel()) - 1) * 16 + 176;
        int v = skill.index * 16 + 128;

        guiGraphics.blit(SkillScreen.RESOURCES, this.getX(), this.getY(),
                176, (ModCapabilities.getSkillModel(minecraft.player).getSkillLevel(this.skill.index) == Config.getMaxLevel() ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0),
                this.width, this.height);

        guiGraphics.blit(SkillScreen.RESOURCES, this.getX() + 6, this.getY() + 8, u, v, 16, 16);
        Font font = minecraft.font;
        String skillName = this.skill.getDisplayName();
        guiGraphics.drawString(font,Component.translatable(skillName),this.getX()+25,this.getY()+7,0xFFFFFF);
        guiGraphics.drawString(font,Component.translatable(ModCapabilities.getSkillModel(minecraft.player).getSkillLevel(this.skill.index) + "/" + Config.getMaxLevel()),this.getX()+25,this.getY()+18,0xFFFFFF);

        if (isMouseOver(mouseX, mouseY) && ModCapabilities.getSkillModel(minecraft.player).getSkillLevel(this.skill.index) < Config.getMaxLevel()) {
            int cost = Config.getStartCost() + (ModCapabilities.getSkillModel(minecraft.player).getSkillLevel(this.skill.index) - 1) * Config.getCostIncrease();
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
