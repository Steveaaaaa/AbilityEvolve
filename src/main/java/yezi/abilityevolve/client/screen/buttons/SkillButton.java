package yezi.abilityevolve.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import yezi.abilityevolve.config.ConfigManager;
import yezi.abilityevolve.client.screen.SkillScreen;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.network.RequestLevelUp;
import yezi.abilityevolve.common.skills.Skill;

public class SkillButton extends Button {
    private final Skill skill;
    private final Minecraft minecraft;
    private final Font font;

    public SkillButton(int x, int y, Skill skill) {
        super(
                x, y, 79, 32,
                Component.empty(),
                onPress -> RequestLevelUp.send(skill),
                Button.DEFAULT_NARRATION
        );
        this.skill = skill;
        this.minecraft = Minecraft.getInstance();
        this.font = minecraft.font;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

        Player player = minecraft.player;
        if (player == null) return;

        SkillModel skillModel = ModCapabilities.getSkillModel(player);
        int skillLevel = skillModel.getSkillLevel(this.skill.index);
        int maxLevel = ConfigManager.getMaxLevel();

        renderButtonBackground(guiGraphics, skillLevel, maxLevel, mouseX, mouseY);
        renderSkillIcon(guiGraphics, skillLevel, maxLevel);
        renderSkillText(guiGraphics, skillLevel, maxLevel);

        if (isMouseOver(mouseX, mouseY) && skillLevel < maxLevel) {
            renderLevelUpCost(guiGraphics, skillLevel, player);
        }
    }

    private void renderButtonBackground(GuiGraphics guiGraphics, int skillLevel, int maxLevel, int mouseX, int mouseY) {
        int vOffset = (skillLevel == maxLevel ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0);
        guiGraphics.blit(SkillScreen.RESOURCES, this.getX(), this.getY(), 176, vOffset, this.width, this.height);
    }

    private void renderSkillIcon(GuiGraphics guiGraphics, int skillLevel, int maxLevel) {
        int u = ((int) Math.ceil((double) skillLevel * 4 / maxLevel) - 1) * 16 + 176;
        int v = skill.index * 16 + 128;
        guiGraphics.blit(SkillScreen.RESOURCES, this.getX() + 6, this.getY() + 8, u, v, 16, 16);
    }

    private void renderSkillText(GuiGraphics guiGraphics, int skillLevel, int maxLevel) {
        String skillName = this.skill.getDisplayName();
        guiGraphics.drawString(font, Component.translatable(skillName), this.getX() + 25, this.getY() + 7, 0xFFFFFF);
        guiGraphics.drawString(font, Component.translatable(skillLevel + "/" + maxLevel), this.getX() + 25, this.getY() + 18, 0xFFFFFF);
    }

    private void renderLevelUpCost(GuiGraphics guiGraphics, int skillLevel, Player player) {
        int cost = ConfigManager.getStartCost() + (skillLevel - 1) * ConfigManager.getCostIncrease();
        int color = player.experienceLevel >= cost ? 0x7EFC20 : 0xFC5454;
        String costText = Integer.toString(cost);
        int textWidth = font.width(costText);
        guiGraphics.drawString(font, costText, this.getX() + 73 - textWidth, this.getY() + 18, color);
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        // Narration logic empty for now.
    }
}
