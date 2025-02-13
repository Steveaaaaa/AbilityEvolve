package yezi.abilityevolve.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.interfaces.IPlayerMixin;
import yezi.abilityevolve.common.interfaces.SpiderClimbing;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IPlayerMixin {

    @Unique
    private int abilityEvolve$chargeTicks = 0;
    @Unique
    private boolean abilityEvolve$wasJumping = false;
    @Unique
    private boolean abilityEvolve$unlocked = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer localPlayer) {
            SpiderClimbing capability = ModCapabilities.getClimbing(localPlayer);

            if (localPlayer.level().isClientSide) {
                BlockHitResult hit = abilityEvolve$detectClimbingSurface(localPlayer);
                boolean shouldClimb = hit.getType() == HitResult.Type.BLOCK &&
                        localPlayer.isShiftKeyDown();

                capability.setClimbing(shouldClimb, hit.getDirection());
            }
            boolean isJumping = localPlayer.input.jumping;
            if (!abilityEvolve$unlocked) {
                if (isJumping && onGround() && !isSwimming())
                    abilityEvolve$executeChargeJump(localPlayer);
            }else {
                if (isJumping && onGround() && !isSwimming()) {
                    //  AbilityEvolve.LOGGER.info("Charge jumping...");
                    ++abilityEvolve$chargeTicks;
                } else if (abilityEvolve$wasJumping && !isJumping && onGround()) {
                    //    AbilityEvolve.LOGGER.info("Jump triggered!");
                    abilityEvolve$executeChargeJump(localPlayer);
                    abilityEvolve$chargeTicks = 0;
                }
                if (abilityEvolve$chargeTicks >= 20) {
                    abilityEvolve$executeChargeJump(localPlayer);
                    abilityEvolve$chargeTicks = 0;
                }
                abilityEvolve$wasJumping = isJumping;
            }
        }
    }

    @Unique
    private void abilityEvolve$executeChargeJump(Player player) {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower()*Math.max(1.0, abilityEvolve$chargeTicks /30.0*3.5), vec3.z);
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
        if(abilityEvolve$unlocked){
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
    @Unique
    private BlockHitResult abilityEvolve$detectClimbingSurface(Player player) {
        return player.level().clip(new ClipContext(
                player.getEyePosition(),
                player.getEyePosition().add(player.getLookAngle().scale(0.5)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
    }
    @Override
    public void abilityEvolve$setAbilityUnlocked(boolean unlocked) {
        this.abilityEvolve$unlocked = unlocked;
    }
}



