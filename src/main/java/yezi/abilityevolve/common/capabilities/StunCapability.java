package yezi.abilityevolve.common.capabilities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.interfaces.IStunCapability;
import yezi.abilityevolve.common.utils.ParticleSpawner;

public class StunCapability implements IStunCapability {
    private long stunEndTime = 0;
    private LivingEntity originalTarget = null;

    @Override
    public boolean isStunned() {
        return System.currentTimeMillis() < stunEndTime;
    }

    @Override
    public void applyStun(LivingEntity entity, double durationSeconds) {
        stunEndTime = System.currentTimeMillis() + (long) (durationSeconds * 1000);

        if (entity instanceof Mob mob) {
            originalTarget = mob.getTarget();
            mob.setTarget(null);
            mob.setAggressive(false);
        }

        int durationTicks = (int) (durationSeconds * 20);
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTicks, 255, false, false, false));
        ParticleSpawner.spawnStunParticles(entity);
        AbilityEvolve.LOGGER.info("实体 " + entity.getUUID() + " 被眩晕 " + durationSeconds + " 秒");
    }

    @Override
    public void removeStun(LivingEntity entity) {
        stunEndTime = 0;
        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

        if (entity instanceof Mob mob) {
            mob.setAggressive(true);
            if (originalTarget != null && originalTarget.isAlive()) {
                mob.setTarget(originalTarget);
            }
        }
        AbilityEvolve.LOGGER.info("实体 " + entity.getUUID() + " 解除眩晕");
    }
}

