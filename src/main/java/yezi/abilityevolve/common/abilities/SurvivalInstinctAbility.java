package yezi.abilityevolve.common.abilities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

public class SurvivalInstinctAbility extends Ability {
    private static final String name = "survival_instinct";
    private static final String description = "Immediately remove negative effects if their duration is less than some what seconds.";

    private static final int[] thresholds = {8, 9, 10, 11, 12};
    private static final int requirement = 26;

    public SurvivalInstinctAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.GATHERING.index, requirement
                        ),
                },
                "defense",
                3,
                6,
                true
        );
    }
    public int getThreshold(Player player) {
        return thresholds[GetAbilityLevel.getAbilityLevelDefense4(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.DEFENSE),requirement)];
    }

    public void checkAndRemoveNegativeEffects(Player player) {
        int threshold = getThreshold(player);

        for (MobEffectInstance effectInstance : player.getActiveEffects()) {
            if (effectInstance.getEffect().isBeneficial()) {
                continue;
            }
            if (effectInstance.getDuration() / 20 < threshold) {
                player.removeEffect(effectInstance.getEffect());
            }
        }
    }
}

