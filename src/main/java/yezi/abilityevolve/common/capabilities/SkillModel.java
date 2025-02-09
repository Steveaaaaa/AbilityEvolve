package yezi.abilityevolve.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import yezi.abilityevolve.common.skills.Skill;

public class SkillModel implements INBTSerializable<CompoundTag> {
    public int[] skillLevels = new int[10];
    private final int[] skillExperience = new int[10];

    public SkillModel() {
        for (int i = 0; i < skillLevels.length; i++) {
            skillLevels[i] = 1;
            skillExperience[i] = 0;
        }
    }

    public int getSkillLevel(int index) {
        return skillLevels[index];
    }

    public void setSkillLevel(int index, int level) {
        skillLevels[index] = level;
    }
    public int getSkillLevel(Skill skill) {
        return skillLevels[skill.index];
    }

    public int getSkillExperience(int index) {
        return skillExperience[index];
    }

    public void setSkillExperience(int index, int experience) {
        skillExperience[index] = experience;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("skillLevels", skillLevels);
        tag.putIntArray("skillExperience", skillExperience);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int[] levels = nbt.getIntArray("skillLevels");
        int[] experience = nbt.getIntArray("skillExperience");
        System.arraycopy(levels, 0, skillLevels, 0, Math.min(levels.length, skillLevels.length));
        System.arraycopy(experience, 0, skillExperience, 0, Math.min(experience.length, skillExperience.length));
    }
}
