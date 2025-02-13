package yezi.abilityevolve.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.interfaces.SpiderClimbing;

@Mixin(Gui.class)
public abstract class HudMixin {
    @Shadow protected abstract Player getCameraPlayer();

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
                    ordinal = 0
            )
    )
    private void adjustCrosshairPosition(PoseStack poseStack, float x, float y, float z) {
        SpiderClimbing climbing = ModCapabilities.getClimbing(getCameraPlayer());
        if (climbing.isClimbing()) {
            float offset = climbing.getDirection() == Direction.DOWN ? -24 : 0;
            poseStack.translate(x, y + offset, z);
        } else {
            poseStack.translate(x, y, z);
        }
    }
}
