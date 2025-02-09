package yezi.abilityevolve.common.skills;

import yezi.abilityevolve.common.capabilities.SkillModel;

public class SkillExperienceHandler {
    private static SkillModel skillModel ;

    public SkillExperienceHandler(SkillModel skillModel) {
        SkillExperienceHandler.skillModel = skillModel;
    }

    public static void addExperience(Skill skill, int experience) {
        skillModel.setSkillExperience(skill.index, skillModel.getSkillExperience(skill.index)+ experience);
        checkForLevelUp(skill);
    }

    private static void checkForLevelUp(Skill skill) {
        while (skillModel.getSkillExperience(skill.index) >= experienceToNextLevel(skillModel.skillLevels[skill.index])) {
            skillModel.setSkillExperience(skill.index, skillModel.getSkillExperience(skill.index) - experienceToNextLevel(skillModel.skillLevels[skill.index]));
            skillModel.setSkillLevel(skill.index, skillModel.getSkillLevel(skill.index)+1);
        }
    }

    private static int experienceToNextLevel(int level) {
        return level * 100;
    }
}