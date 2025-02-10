package yezi.abilityevolve.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.abilityevolve.common.capabilities.ModCapabilities;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void onAiStep(CallbackInfo ci) {
        this.getCapability(ModCapabilities.STUN).ifPresent(stun -> {
            if (stun.isStunned()) {
                this.setDeltaMovement(Vec3.ZERO);
                this.hurtMarked = true;
                ci.cancel();
            }
        });
    }
}

