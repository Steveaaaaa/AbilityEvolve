package yezi.abilityevolve.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.interfaces.SpiderClimbing;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow public abstract PreparableReloadListener createReloadListener();

    @Redirect(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;getXRot()F"
            )
    )
    private float adjustXRot(Camera camera) {
        SpiderClimbing climbing = ModCapabilities.getClimbing((LivingEntity) camera.getEntity());
        if (climbing.isClimbing() &&
                climbing.getDirection() == Direction.DOWN) {
            return -camera.getXRot();
        }
        return camera.getXRot();
    }

    @Redirect(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;getYRot()F"
            )
    )
    private float adjustYRot(Camera camera) {
        SpiderClimbing climbing = ModCapabilities.getClimbing((LivingEntity) camera.getEntity());
        if (climbing.isClimbing() &&
                climbing.getDirection() == Direction.DOWN) {
            return camera.getYRot() + 180.0F;
        }
        return camera.getYRot();
    }
}
