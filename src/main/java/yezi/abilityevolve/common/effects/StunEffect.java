package yezi.abilityevolve.common.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.utils.ParticleSpawner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StunEffect {
    private static final Map<UUID, LivingEntity> originalTargets = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void apply(LivingEntity entity, double durationSeconds) {
        if (entity == null || entity.isRemoved()) return;

        UUID uuid = entity.getUUID();
        int durationTicks = (int) (durationSeconds * 20);
        AbilityEvolve.LOGGER.info("应用 " + durationSeconds + " 秒眩晕在 " + uuid);

        if (!entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTicks, 255, false, false, false));
        }

        ParticleSpawner.spawnStunParticles(entity);

        if (entity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null) {
                originalTargets.put(uuid, target);
                mob.setTarget(null);
            }
            mob.setAggressive(false);
        }

        scheduler.schedule(() -> Minecraft.getInstance().execute(() -> removeStun(entity)),
                (long) (durationSeconds * 1000), TimeUnit.MILLISECONDS);
    }

    private static void removeStun(LivingEntity entity) {
        if (entity == null || entity.isRemoved()) return;

        UUID uuid = entity.getUUID();
        AbilityEvolve.LOGGER.info("实体 " + uuid + " 解除眩晕");
        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

        if (entity instanceof Mob mob) {
            mob.setAggressive(true);
            LivingEntity originalTarget = originalTargets.remove(uuid);
            if (originalTarget != null && originalTarget.isAlive()) {
                mob.setTarget(originalTarget);
            }
        }
    }
}

