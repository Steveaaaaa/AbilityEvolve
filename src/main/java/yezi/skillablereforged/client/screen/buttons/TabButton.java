package yezi.skillablereforged.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import yezi.skillablereforged.client.screen.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TabButton extends AbstractWidget {
    private final boolean selected;
    private final TabType type;

    public TabButton(int x, int y, TabType type, boolean selected) {
        super(x, y, 31, 28, Component.literal("Skill"));
        this.type = type;
        this.selected = selected;
    }
    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        this.active = !(minecraft.screen instanceof InventoryScreen) || !((InventoryScreen)minecraft.screen).getRecipeBookComponent().isVisible();
        if (this.active) {
            RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
            guiGraphics.blit(SkillScreen.RESOURCES, this.getX(), this.getY(), this.selected ? 31 : 0, 166, this.width, this.height);
            guiGraphics.blit(SkillScreen.RESOURCES, this.getX() + (this.selected ? 8 : 10), this.getY() + 6, 240, 128 + this.type.iconIndex * 16, 16, 16);
        }
    }

    public void onPress()
    {
        Minecraft minecraft = Minecraft.getInstance();

        switch (type)
        {
            case INVENTORY -> {
                assert minecraft.player != null;
                minecraft.setScreen(new InventoryScreen(minecraft.player));
            }
            case SKILLS -> minecraft.setScreen(new SkillScreen());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }


    public static enum TabType {
        INVENTORY(0),
        SKILLS(1);
        public final int iconIndex;

        private TabType(int index) {
            this.iconIndex = index;
        }
    }
}