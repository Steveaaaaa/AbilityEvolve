package yezi.abilityevolve.common.abilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

public class BlindsideAbility extends Ability{
    private static final String name = "blindside";
    private static final String description = "If the target does not detect you, the damage increase";
    private static final int requirement = 16;

    public BlindsideAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.ATTACK.index, requirement
                        ),
                },
                "attack",
                1,
                6,
                true
        );
    }
    private static final double[] DAMAGE_MULTIPLIER = {1.1, 1.2, 1.4, 1.5, 1.7, 1.8, 1.9, 2.1, 2.2, 2.4};

    public float getBonusDamage(LivingEntity target, ServerPlayer player) {
        if (target instanceof Mob mob) {
            LivingEntity targetAttacker = mob.getTarget();
            if (!(targetAttacker instanceof ServerPlayer)) {
                return (float) DAMAGE_MULTIPLIER[GetAbilityLevel.getAbilityLevelAttack1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.MINING), requirement)-1];
            }
        }
        return 1.0f;
    }
}

