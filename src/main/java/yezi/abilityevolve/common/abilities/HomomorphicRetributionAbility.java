package yezi.abilityevolve.common.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HomomorphicRetributionAbility extends Ability{
    private static final String name = "homomorphic_retribution";
    private static final String description = "Make mobs around you fired when you are in hot stuff.";
    private static final int requirement = 12;

   // int abilityLevel = GetAbilityLevel.getAbilityLevelDefense1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.DEFENSE), requirement);
    public HomomorphicRetributionAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.DEFENSE.index, requirement
                        ),
                },
                "defense",
                0,
                4,
                true
        );
    }
    private final Map<UUID, ScheduledFuture<?>> activeTimers = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public double getRange(Player player) {
        return GetAbilityLevel.getAbilityLevelDefense1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.DEFENSE), requirement) * 0.5 + 2.0;
    }
    public void startAbility(Player player, boolean isInSoulFire, boolean isInLava) {
        if (activeTimers.containsKey(player.getUUID())) return;
        UUID playerUUID = player.getUUID();
        double range = getRange(player);

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            if (!player.isAlive() || player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                stopAbility(player);
                return;
            }

            Level world = player.level();
            BlockPos playerPos = player.blockPosition();
            AABB area = new AABB(playerPos).inflate(range);

            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, area)) {
                if (entity instanceof Mob) {
                    entity.setSecondsOnFire(2);
                    if (isInLava || isInSoulFire) {
                        int damage = isInSoulFire ? 2 : 4;
                        entity.hurt(world.damageSources().onFire(), damage);
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        activeTimers.put(playerUUID, task);
    }

    public void stopAbility(Player player) {
        UUID playerUUID = player.getUUID();
        ScheduledFuture<?> task = activeTimers.remove(playerUUID);
        if (task != null) {
            task.cancel(true);
        }
    }
}

