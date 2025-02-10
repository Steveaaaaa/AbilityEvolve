package yezi.abilityevolve.common.abilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.effects.StunEffect;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;
import yezi.abilityevolve.common.utils.Pair;
import yezi.abilityevolve.common.utils.ParticleSpawner;

import java.util.*;

public class LeapStrikeAbility extends Ability {
    private static final String name = "leap_strike";
    private static final String description = "长按空格以蓄力，蓄力后跳跃高度增加，跃斩击中目标可造成额外伤害和晕眩效果";
    private static final int requirement = 18;

  //  public int abilityLevel = GetAbilityLevel.getAbilityLevelAttack2(ModCapabilities.getSkillModel(Minecraft.getInstance().player).getSkillLevel(Skill.ATTACK), requirement);
    public Set<UUID> leapingPlayers = new HashSet<>();
    private final Map<Integer, Pair<Double, Double>> levelEffects = new HashMap<>(); // 存储伤害加成和眩晕的对应关系

    public LeapStrikeAbility() {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.ATTACK.index, requirement),
                        new Requirement(Skill.AGILITY.index, 10)
                },
                "attack",
                2,
                6,
                true
        );
        levelEffects.put(1, new Pair<>(1.4, 1.0));
        levelEffects.put(2, new Pair<>(1.5, 1.3));
        levelEffects.put(3, new Pair<>(1.6, 1.6));
        levelEffects.put(4, new Pair<>(1.7, 1.9));
        levelEffects.put(5, new Pair<>(1.8, 2.2));
        levelEffects.put(6, new Pair<>(1.9, 2.5));
        levelEffects.put(7, new Pair<>(2.05, 2.6));
        levelEffects.put(8, new Pair<>(2.2, 2.7));
        levelEffects.put(9, new Pair<>(2.35, 2.8));
        levelEffects.put(10, new Pair<>(2.5, 3.0));
    }

    public void triggerLeapStrike(ServerPlayer player, LivingEntity target) {
        if (!levelEffects.containsKey(abilityLevel) || player.onGround()) return;
        UUID uuid = player.getUUID();
        leapingPlayers.add(uuid);
        int skillLevel = ModCapabilities.getSkillModel(player).getSkillLevel(Skill.ATTACK);
        int abilityLevel = GetAbilityLevel.getAbilityLevelAttack2(skillLevel, requirement);
        if (!levelEffects.containsKey(abilityLevel)) return;

        Pair<Double, Double> effect = levelEffects.get(abilityLevel);
        double damageMultiplier = effect.getLeft();
        double stunDuration = effect.getRight();
        double baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        AbilityEvolve.LOGGER.info("跃斩伤害：" + baseDamage + baseDamage * (damageMultiplier - 1.0));

        ParticleSpawner.spawnImpactParticles(target.position());

        AABB area = new AABB(target.blockPosition()).inflate(5.0);
        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != player);

        for (LivingEntity entity : entities) {
            AbilityEvolve.LOGGER.info("晕眩目标：" + entity.getName().getString());
            entity.hurt(player.damageSources().playerAttack(player), (float) (baseDamage * (damageMultiplier - 1.0)));
            applyStun(entity, stunDuration);
        }
        leapingPlayers.remove(uuid);
    }

    private void applyStun(LivingEntity entity, double duration) {
        StunEffect.apply(entity, duration);
    }
}
