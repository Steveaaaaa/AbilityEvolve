package yezi.abilityevolve.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.particles.AbilityEvolveParticle;
import yezi.abilityevolve.common.particles.ShieldParticleData;

import java.util.*;
@Mod.EventBusSubscriber(modid = AbilityEvolve.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ParticleSpawner {
    private static final Map<Vec3, Integer> activeExplosions = new HashMap<>();
    private static final Map<UUID, Integer> entityStunTicks = new HashMap<>();
    private static final List<RingEffect> activeRings = new ArrayList<>();

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

    public static void spawnResistanceEffect(Vec3 playerPos, Vec3 attackerPos) {
        activeRings.add(new RingEffect(playerPos, attackerPos, 0.0f, (float) (Math.PI / 2), 0.07f));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<RingEffect> it = activeRings.iterator();
            while (it.hasNext()) {
                RingEffect effect = it.next();
                updateRingEffect(effect);
                if (effect.completed) {
                    it.remove();
                }
            }
        }
    }


 // 根据当前阶段生成粒子（扩散 → 保持 → 淡出）

    private static void updateRingEffect(RingEffect effect) {
        if (Minecraft.getInstance().level == null) return;

        Level level = Minecraft.getInstance().level;
        Vec3 playerPos = effect.playerPos;
        Vec3 attackerPos = effect.attackerPos;
        float expansionThreshold = effect.maxTheta*0.8f;

        // 粒子数量参数
        int baseParticles = 10;
        int additionalParticles = 20;

        float alpha;
        double sphereRadius;
        int numParticles;

        if (!effect.expansionStopped) {
            float theta = effect.currentTheta;
            //可调参数：进入保持阶段的阈值
            if (theta >= expansionThreshold || (expansionThreshold - theta) < 0.05f) {
                effect.expansionStopped = true;
                effect.currentTheta = expansionThreshold;
                effect.holdTime = 0;
                alpha = 1.0f;
                numParticles = baseParticles + additionalParticles;
            } else {
                float decelerationFactor = 1.0f - (theta / expansionThreshold);
                effect.currentTheta += effect.growthSpeed * decelerationFactor;
                alpha = 1.0f;
                numParticles = baseParticles + (int)(additionalParticles * (theta / expansionThreshold));
            }
            sphereRadius = 0.7 + effect.currentTheta * 0.25;
        } else if (effect.holdTime < effect.maxHoldTime) {
            effect.holdTime++;
            sphereRadius = 0.7 + effect.currentTheta * 0.25;
            numParticles = baseParticles + additionalParticles;
            alpha = 1.0f;
        } else {
            effect.fadeTime++;
            float fadeProgress = effect.fadeTime / (float) effect.maxFadeTime;
            if (fadeProgress > 1.0f) fadeProgress = 1.0f;
            alpha = (float) Math.pow(1.0 - fadeProgress, 2);
            sphereRadius = 0.7 + effect.currentTheta * 0.25;
            numParticles = baseParticles + additionalParticles;
            if (fadeProgress >= 1.0f) {
                effect.completed = true;
            }
        }

        Vec3 front = attackerPos.subtract(playerPos).normalize();
        Vec3 upRef = new Vec3(0, 1, 0);
        if (Math.abs(front.dot(upRef)) > 0.95) {
            upRef = new Vec3(0, 0, 1);
        }
        Vec3 right = front.cross(upRef).normalize();
        Vec3 up = right.cross(front).normalize();

        for (int i = 0; i < numParticles; i++) {
            double phi = 2 * Math.PI * i / numParticles;
            double xLocal = Math.sin(effect.currentTheta) * Math.cos(phi);
            double yLocal = Math.sin(effect.currentTheta) * Math.sin(phi);
            double zLocal = Math.cos(effect.currentTheta);

            Vec3 worldDir = right.scale(xLocal).add(up.scale(yLocal)).add(front.scale(zLocal));
            Vec3 particlePos = playerPos.add(worldDir.scale(sphereRadius)).add(0, 1.2, 0);

            Vec3 velocity;
            if (!effect.expansionStopped) {
                Vec3 tangent = right.scale(-Math.sin(phi)).add(up.scale(Math.cos(phi)));
                velocity = tangent.scale(0.2);
            } else {
                velocity = new Vec3(0, 0, 0);
            }

            level.addParticle(
                    new ShieldParticleData(alpha),
                    particlePos.x(), particlePos.y(), particlePos.z(),
                    velocity.x(), velocity.y(), velocity.z()
            );
        }
    }

    private static class RingEffect {
        final Vec3 playerPos;
        final Vec3 attackerPos;
        float currentTheta;   // 当前扩散角度
        final float maxTheta; // 扩散阶段目标角度
        final float growthSpeed; // 基础增长速度

        boolean expansionStopped = false;
        int holdTime = 0;
        final int maxHoldTime = 20; // 保持阶段总时长
        int fadeTime = 0;
        final int maxFadeTime = 10; // 淡出阶段总时长
        boolean completed = false;

        RingEffect(Vec3 playerPos, Vec3 attackerPos, float currentTheta, float maxTheta, float growthSpeed) {
            this.playerPos = playerPos;
            this.attackerPos = attackerPos;
            this.currentTheta = currentTheta;
            this.maxTheta = maxTheta;
            this.growthSpeed = growthSpeed;
        }
    }
}
