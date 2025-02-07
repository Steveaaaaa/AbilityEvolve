package yezi.skillablereforged.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.skillablereforged.Skillablereforged;

@Mixin(Player.class)
public abstract class PlayerChargeJumpMixin extends LivingEntity {
    @Shadow public abstract void awardStat(Stat<?> pStat);

    private int chargeTicks = 0;
    private boolean wasJumping = false;

    protected PlayerChargeJumpMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer localPlayer) {
            boolean isJumping = localPlayer.input.jumping;
            if (isJumping && onGround() && !isSwimming()) {
                Skillablereforged.LOGGER.info("Charge jumping...");
                chargeTicks++;
            } else if (wasJumping && !isJumping && onGround()) {
                Skillablereforged.LOGGER.info("Jump triggered!");
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
        this.hasImpulse = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void cancelVanillaJump(CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer) {
            ci.cancel();
        }
    }
}



