package yezi.abilityevolve.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.interfaces.SpiderClimbing;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravel(Vec3 movement, CallbackInfo ci) {
        if ((Object)this instanceof Player player) {
            SpiderClimbing capability = ModCapabilities.getClimbing(player);

            if (capability.isClimbing()) {
                abilityEvolve$handleClimbingMovement(player, capability.getDirection(), movement);
                ci.cancel();
            }
        }
    }

    @Unique
    private void abilityEvolve$handleClimbingMovement(Player player, Direction dir, Vec3 movement) {
        player.setNoGravity(true);

        Vec3 adjusted = switch (dir) {
            case DOWN -> new Vec3(movement.x * 0.8, -0.15, movement.z * 0.8);
            case UP -> new Vec3(movement.x * 0.8, 0.15, movement.z * 0.8);
            default -> movement.scale(0.8);
        };

        player.setDeltaMovement(adjusted);
    }
}
