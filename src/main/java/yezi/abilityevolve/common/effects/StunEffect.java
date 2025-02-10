package yezi.abilityevolve.common.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.utils.ParticleSpawner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StunEffect {
    private static final Map<UUID, Long> stunnedEntities = new ConcurrentHashMap<>();
    private static final Map<UUID, LivingEntity> originalTargets = new ConcurrentHashMap<>();

    public static void apply(LivingEntity entity, double durationSeconds) {
        if (entity == null || entity.isRemoved()) return;

        UUID uuid = entity.getUUID();
        long expireTime = System.currentTimeMillis() + (long) (durationSeconds * 1000);
        stunnedEntities.put(uuid, expireTime);
        int durationTicks = (int) (durationSeconds * 20);
        AbilityEvolve.LOGGER.info("应用了 " + durationSeconds + " 秒的眩晕效果在实体 " + uuid + " 上");
        if (!entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTicks, 255, false, false, false));
        }


        if (entity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null) {
                originalTargets.put(uuid, target);
                mob.setTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.isRemoved() || !entity.isAlive()) return;

        UUID uuid = entity.getUUID();
        Long expireTime = stunnedEntities.get(uuid);
        if (expireTime == null) return;

        if (System.currentTimeMillis() > expireTime) {
            stunnedEntities.remove(uuid);
            removeStun(entity);
        } else {
            ParticleSpawner.spawnStunParticles(entity);
            entity.setDeltaMovement(Vec3.ZERO);
            entity.hurtMarked = true;
            if (entity instanceof Mob mob) {
                if (mob.getTarget() != null) {
                    mob.setTarget(null);
                }
                mob.setAggressive(false);
            }
        }
    }

    private static void removeStun(LivingEntity entity) {
        if (entity == null || entity.isRemoved()) return;

        UUID uuid = entity.getUUID();
        AbilityEvolve.LOGGER.info("实体 " + uuid + " 解除了眩晕");
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

