package yezi.abilityevolve.common.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;

public class HomomorphicRetributionAbility extends Ability{
    private static final String name = "homomorphic_retribution";
    private static final String description = "Make mobs around you fired when you are in hot stuff.";
    private static final int requirement = 12;

    public int abilityLevel = GetAbilityLevel.getAbilityLevelDefense1(ModCapabilities.getSkillModel(Minecraft.getInstance().player).getSkillLevel(Skill.DEFENSE), requirement);
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
    private final Map<Player, Long> lastDamageTimeMap = new HashMap<>();
    public double getRange() {
        return abilityLevel * 0.5 + 2.0;
    }
    public void applyEffect(Player player){
        if (player.hasEffect(MobEffects.FIRE_RESISTANCE))
            return;
        Level world = player.level();
        BlockPos playerPos = player.blockPosition();
        double range = getRange();
        AABB area = new AABB(playerPos).inflate(range);
        for (Entity entity : world.getEntities(player, area)) {
            if (entity instanceof LivingEntity livingEntity && entity != player) {
                if (isInFire(playerPos, world)) {
                    livingEntity.setSecondsOnFire(1);
                }

                if (isInSoulFireOrLava(playerPos, world)) {
                    long currentTime = System.currentTimeMillis();
                    long lastDamageTime = lastDamageTimeMap.getOrDefault(player, 0L);

                    if (currentTime - lastDamageTime >= 1000){
                        livingEntity.setSecondsOnFire(1);
                        int damage = isInSoulFire(playerPos, world) ? 2 : 4;
                        livingEntity.hurt(world.damageSources().onFire(), damage);
                    }
                }
            }
        }
    }
    private boolean isInFire(BlockPos pos, Level world) {
        return world.getBlockState(pos).getBlock() == Blocks.FIRE;
    }

    private boolean isInSoulFireOrLava(BlockPos pos, Level world) {
        return world.getBlockState(pos).getBlock() == Blocks.SOUL_FIRE ||
                world.getFluidState(pos).getType() == Fluids.LAVA;
    }

    private boolean isInSoulFire(BlockPos pos, Level world) {
        return world.getBlockState(pos).getBlock() == Blocks.SOUL_FIRE;
    }
}
