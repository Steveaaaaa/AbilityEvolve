package yezi.abilityevolve.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.particles.AbilityEvolveParticle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ParticleSpawner {
    private static final Map<Vec3, Integer> activeExplosions = new HashMap<>();
    private static final Map<UUID, Integer> entityStunTicks = new HashMap<>();
    public static void spawnImpactParticles(Vec3 position) {
        activeExplosions.put(position, 40); // **持续 60 tick（3秒）**
    }
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (activeExplosions.isEmpty()) return;

        Iterator<Map.Entry<Vec3, Integer>> iterator = activeExplosions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vec3, Integer> entry = iterator.next();
            Vec3 pos = entry.getKey();
            int remainingTicks = entry.getValue();

            ServerLevel level = (ServerLevel) event.getEntity().level();

            for (int i = 0; i < 20; i++) {
                double angle = Math.toRadians(i * (360.0 / 20));
                double radius = 2.5 + Math.random() * 1.5;
                double xOffset = Math.cos(angle) * radius;
                double zOffset = Math.sin(angle) * radius;

                level.sendParticles(ParticleTypes.EXPLOSION,
                        pos.x + xOffset, pos.y, pos.z + zOffset,
                        1, 0, 0, 0, 0);

                level.sendParticles(ParticleTypes.CLOUD,
                        pos.x + xOffset, pos.y + 0.2, pos.z + zOffset,
                        1, 0, 0, 0, 0);

                level.sendParticles(ParticleTypes.SWEEP_ATTACK,
                        pos.x + xOffset, pos.y + 0.5, pos.z + zOffset,
                        1, 0, 0, 0, 0);
            }
            remainingTicks--;
            if (remainingTicks <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remainingTicks);
            }
        }
    }
    public static void spawnStunParticles(LivingEntity entity) {
        UUID uuid = entity.getUUID();
        int tick = entityStunTicks.getOrDefault(uuid, 0);
        entityStunTicks.put(uuid, tick + 1);

        double yOffset = entity.getBbHeight() * 1.15;

        for (int i = 0; i < 4; i++) {
            double angle = Math.toRadians((tick * 5 + i * 90) % 360);
            double radius = 0.5;
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            entity.level().addParticle(AbilityEvolveParticle.YELLOW_STAR.get(),
                    entity.getX() + xOffset,
                    entity.getY() + yOffset,
                    entity.getZ() + zOffset,
                    0, 0.03, 0);

        }
    }
    public static void createFireRing(Level world, BlockPos playerPos, double range) {
        int numParticles = 40;
        double angleStep = Math.PI * 2 / numParticles;

        for (int i = 0; i < numParticles; i++) {
            double angle = i * angleStep;
            double xOffset = Math.sin(angle) * range;
            double zOffset = Math.cos(angle) * range;

            double x = playerPos.getX() + xOffset;
            double y = playerPos.getY() + 0.5;
            double z = playerPos.getZ() + zOffset;

            world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
}
