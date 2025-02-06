package yezi.skillablereforged.common.abilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.effects.StunEffect;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.utils.GetAbilityLevel;
import yezi.skillablereforged.common.utils.Pair;

import java.util.*;

public class LeapStrikeAbility extends Ability{
    private static final String name = "leap_strike";
    private static final String description = "长按空格以蓄力，蓄力后跳跃更高，跃斩击中目标可造成额外伤害和晕眩效果";
    private static final int requirement = 18;
    GetAbilityLevel getAbilityLevel = new GetAbilityLevel();

    public int abilityLevel = getAbilityLevel.getAbilityLevelAttack2(SkillModel.get().getSkillLevel(Skill.ATTACK), requirement);
    private final int maxChargeTime = 20;
    private final Map<UUID, Integer> chargeMap = new HashMap<>();
    private final Set<UUID> leapingPlayers = new HashSet<>();
    private final Set<UUID> jumpingPlayers = new HashSet<>();
    private final Map<Integer, Pair<Double, Double>> levelEffects = new HashMap<>(); // 存储伤害加成和眩晕的对应关系
    public LeapStrikeAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.ATTACK, requirement),
                        new Requirement(Skill.AGILITY,10)
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
    public void startCharging(ServerPlayer player) {
        chargeMap.put(player.getUUID(), 0);
    }
    public void chargeTick(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (chargeMap.containsKey(uuid)) {
            int chargeTime = chargeMap.get(uuid) + 1;
            chargeMap.put(uuid, Math.min(chargeTime, maxChargeTime));
        }
    }
    public void performLeap(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (!chargeMap.containsKey(uuid)) return;
        int chargeTime = chargeMap.get(uuid);
        double jumpPower = 0.5 + (chargeTime / (double) maxChargeTime) * 3.0;
        player.setDeltaMovement(player.getDeltaMovement().x, jumpPower, player.getDeltaMovement().z);
        player.fallDistance = 0;
        chargeMap.remove(uuid);
        leapingPlayers.add(uuid);
        jumpingPlayers.add(uuid);
    }
    public void triggerLeapStrike(ServerPlayer player, LivingEntity target) {
        UUID uuid = player.getUUID();
        if (!levelEffects.containsKey(abilityLevel)) return;
        if (!leapingPlayers.contains(uuid)) return;
        Pair<Double, Double> effect = levelEffects.get(abilityLevel);

        double damageMultiplier = effect.getLeft();
        double stunDuration = effect.getRight();

        double baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        target.hurt(player.damageSources().playerAttack(player), (float) (baseDamage * (damageMultiplier - 1.0)));

        AABB area = new AABB(target.blockPosition()).inflate(5.0);
        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != player);

        for (LivingEntity entity : entities) {
            applyStun(entity, stunDuration);
        }
        leapingPlayers.remove(uuid);
    }
    private void applyStun(LivingEntity entity, double duration) {
        StunEffect.apply(entity, duration);
    }
    public void onPlayerLand(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (leapingPlayers.contains(uuid)|| jumpingPlayers.contains(uuid)) {
            leapingPlayers.remove(uuid);
            jumpingPlayers.remove(uuid);
        }
    }
}
