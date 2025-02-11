package yezi.abilityevolve.common.abilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

public class SurvivorAbility extends Ability {
    private static final String name = "survivor";
    private static final String description = "Reduce damage taken from undead creatures.";
    private static final int requirement = 22;

    private static final float[] DAMAGE_REDUCTION = {0.12f, 0.16f, 0.19f, 0.22f, 0.25f, 0.3f};

    public SurvivorAbility() {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.DEFENSE.index, requirement)
                },
                "defense",
                2,
                6,
                true
        );
    }

    public float getDamageReduction(Player player) {
        int abilityLevel = GetAbilityLevel.getAbilityLevelDefense2(
                ModCapabilities.getSkillModel(player).getSkillLevel(Skill.DEFENSE),
                requirement
        );

        return abilityLevel > 0 ? DAMAGE_REDUCTION[abilityLevel - 1] : 0.0f;
    }

    public boolean isUndead(LivingEntity attacker) {
        return attacker instanceof Zombie ||
                attacker instanceof Skeleton ||
                attacker instanceof WitherSkeleton ||
                attacker instanceof Stray ||
                attacker instanceof Drowned ||
                attacker instanceof Phantom ||
                attacker instanceof Husk ||
                attacker instanceof WitherBoss ||
                attacker instanceof ZombifiedPiglin;
    }

    public float applyDamageReduction(Player player, float originalDamage) {
        float reduction = getDamageReduction(player);
        return originalDamage * (1 - reduction);
    }
}
