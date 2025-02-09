package yezi.abilityevolve.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import yezi.abilityevolve.client.screen.buttons.SkillButton;
import yezi.abilityevolve.common.skills.Skill;

public class SkillScreen extends Screen {
    public static final ResourceLocation RESOURCES = new ResourceLocation("abilityevolve", "textures/gui/skills.png");

    public SkillScreen() {
        super(Component.translatable("container.skills")
                .setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.BLACK)));
    }
    @Override
    protected void init() {
        int left = (this.width - 162) / 2;
        int top = (this.height - 128) / 2;

        for (int i = 0; i < 8; ++i) {
            int x = left + (i % 2) * 83;
            int y = top + (i / 2) * 36;

            Skill skill = Skill.values()[i];

            this.addRenderableWidget(new SkillButton(x, y, skill));
        }
    }

   /* private int getLevel(Skill skill) {
        if (Minecraft.getInstance().player!= null) {
            return ModCapabilities.getSkillModel(Minecraft.getInstance().player).getSkillLevel(skill.index);
        } else {
            return 0;
        }
    }

    private int getMaxLevel() {
        return Config.getMaxLevel();
    }*/
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
