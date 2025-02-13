package yezi.abilityevolve.common.abilities;

import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

public class WireAbility extends Ability{
    private static final String name = "wire";
    private static final String description = "攀附";
    private static final int requirement = 12;
    public WireAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.BUILDING.index, requirement
                        ),
                        new Requirement(
                                Skill.AGILITY.index, 8
                        )
                },
                "building",
                0,
                6,
                false
        );
    }
}
