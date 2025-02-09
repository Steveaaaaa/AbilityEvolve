package yezi.abilityevolve.common.abilities;

import net.minecraft.client.Minecraft;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

public class HomomorphicRetributionAbility extends Ability{
    private static final String name = "homomorphic_retribution";
    private static final String description = "Make mobs around you fired when you are in lava.";
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
}
