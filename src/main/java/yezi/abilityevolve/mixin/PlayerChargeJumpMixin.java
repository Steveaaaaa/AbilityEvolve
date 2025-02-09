package yezi.abilityevolve.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.abilityevolve.common.interfaces.IPlayerMixin;

@Mixin(Player.class)
public abstract class PlayerChargeJumpMixin extends LivingEntity implements IPlayerMixin {

    private int chargeTicks = 0;
    private boolean wasJumping = false;
    private boolean unlocked = false;

    protected PlayerChargeJumpMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer localPlayer) {
            boolean isJumping = localPlayer.input.jumping;
            if (!unlocked) {
                if (isJumping && onGround() && !isSwimming())
                    executeChargeJump(localPlayer);
            }else {
                if (isJumping && onGround() && !isSwimming()) {
                    //  AbilityEvolve.LOGGER.info("Charge jumping...");
                    ++chargeTicks;
                } else if (wasJumping && !isJumping && onGround()) {
                    //    AbilityEvolve.LOGGER.info("Jump triggered!");
                    executeChargeJump(localPlayer);
                    chargeTicks = 0;
                }
                if (chargeTicks >= 20) {
                    executeChargeJump(localPlayer);
                    chargeTicks = 0;
                }
                wasJumping = isJumping;
            }
        }
    }

    private void executeChargeJump(Player player) {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower()*Math.max(1.0, chargeTicks/30.0*3.5), vec3.z);
        if (this.isSprinting()) {
            float f = this.getYRot() * ((float)Math.PI / 180F);
            this.setDeltaMovement(this.getDeltaMovement().add(-Mth.sin(f) * 0.2F, 0.0D, Mth.cos(f) * 0.2F));
        }
        player.awardStat(Stats.JUMP);
        if (this.isSprinting()) {
            player.causeFoodExhaustion(0.2F);
        } else {
            player.causeFoodExhaustion(0.05F);
        }
        if(unlocked){
            for (int i = 0; i < 5; i++) {
                player.level().addParticle(ParticleTypes.CLOUD,
                        player.getX(), player.getY(), player.getZ(),
                        (Math.random() - 0.5) * 0.5, 0.2, (Math.random() - 0.5) * 0.5);
            }
        }
        this.hasImpulse = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void cancelVanillaJump(CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer) {
            ci.cancel();
        }
    }
    @Override
    public void setAbilityUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}



