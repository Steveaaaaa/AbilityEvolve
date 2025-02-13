package yezi.abilityevolve.mixin;

import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.abilityevolve.common.abilities.SpiderClimbingImpl;
import yezi.abilityevolve.common.capabilities.ModCapabilities;

@OnlyIn(Dist.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private float xRot;
    @Shadow private float yRot;
    @Shadow private Vec3 position;
    @Shadow private Entity entity;

    @Shadow protected abstract void setPosition(Vec3 pos);
    @Shadow protected abstract void setRotation(float yRot, float xRot);

    @Inject(method = "setup", at = @At("TAIL"))
    private void onSetup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        SpiderClimbingImpl climbing = (SpiderClimbingImpl) ModCapabilities.getClimbing((LivingEntity)entity);
        if (climbing.isClimbing()) {
            Direction dir = climbing.getDirection();
            adjustCameraForClimbing(dir, partialTick);
        }
    }

    @Unique
    private void adjustCameraForClimbing(Direction dir, float partialTick) {
        if (dir == Direction.DOWN) {
            float targetXRot = -this.xRot;
            float targetYRot = this.yRot + 180.0F;

            float lerpFactor = 0.2f * partialTick * 20;
            float newXRot = Mth.lerp(lerpFactor, this.xRot, targetXRot);
            float newYRot = Mth.lerp(lerpFactor, this.yRot, targetYRot);

            this.setRotation(newYRot % 360, newXRot);

            Vec3 eyeOffset = new Vec3(0, entity.getEyeHeight() * 0.85, 0)
                    .xRot((float)Math.toRadians(-newXRot))
                    .yRot((float)Math.toRadians(-newYRot));

            this.setPosition(this.position.add(eyeOffset));
        }
    }

    @ModifyVariable(
            method = "getMaxZoom",
            at = @At(value = "STORE", ordinal = 0),
            argsOnly = true
    )
    private double adjustCollisionDistance(double original) {
        if (ClimbingStateHandler.isClimbing()) {
            return original * 1.8;
        }
        return original;
    }
}
