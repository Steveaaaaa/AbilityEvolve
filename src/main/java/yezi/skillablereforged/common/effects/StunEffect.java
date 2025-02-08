package yezi.skillablereforged.common.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.utils.ParticleSpawner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StunEffect {
    private static final Map<UUID, Long> stunnedEntities = new HashMap<>();
    private static final Map<UUID, LivingEntity> originalTargets = new HashMap<>();

    public static void apply(LivingEntity entity, double durationSeconds) {
        long expireTime = System.currentTimeMillis() + (long) (durationSeconds * 1000);
        stunnedEntities.put(entity.getUUID(), expireTime);
        int durationTicks = (int) (durationSeconds * 20);
        Skillablereforged.LOGGER.info("应用了" + durationSeconds + "秒的眩晕效果在xx实体上" + entity.getUUID());
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTicks, 255, false, false, false));
        if (entity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null) {
                originalTargets.put(entity.getUUID(), target);
            }
        }
    }
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        UUID uuid = entity.getUUID();
        if (!stunnedEntities.containsKey(uuid)) return;
        long expireTime = stunnedEntities.get(uuid);
        if (System.currentTimeMillis() > expireTime) {
            stunnedEntities.remove(uuid);
            removeStun(entity);
        } else {
            entity.setDeltaMovement(Vec3.ZERO);
            entity.hurtMarked = true;
            ParticleSpawner.spawnStunParticles(entity);
            if (entity instanceof Mob mob) {
                mob.setTarget(null);
                mob.setAggressive(false);
            }
        }
    }
    private static void removeStun(LivingEntity entity) {
        Skillablereforged.LOGGER.info("实体" + entity.getUUID() + "解除了眩晕");
        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (entity instanceof Mob mob) {
            mob.setAggressive(true);
            UUID uuid = entity.getUUID();
            if (originalTargets.containsKey(uuid)) {
                LivingEntity originalTarget = originalTargets.get(uuid);
                if (originalTarget.isAlive()) {
                    mob.setTarget(originalTarget);
                }
                originalTargets.remove(uuid);
            }
        }
    }
}
