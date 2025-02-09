package yezi.abilityevolve.common.abilities;

import yezi.abilityevolve.common.skills.Requirement;

public abstract class Ability {
    public final String name;
    public final String description;
    public final Requirement[] requiredSkill;
    public final String abilityType;
    public final int index;
    public final int skillPointCost;
    public int abilityLevel;
    public int isUnlocked;
    public boolean canUnlock;
    public boolean Passive;
    public Ability(String name, String description, Requirement[] requiredSkill,String abilityType,int index, int skillPointCost, boolean passive) {
        this.name = name;
        this.description = description;
        this.requiredSkill = requiredSkill;
        this.abilityType = abilityType;
        this.index = index;
        this.skillPointCost = skillPointCost;
        this.abilityLevel = 0;
        this.isUnlocked = 0;
        this.canUnlock = false;
        this.Passive = passive;
    }
    private void setUnlocked(boolean unlocked) {
        this.isUnlocked = 1;
    }
    private void setCanUnlock(boolean canUnlock) {
        this.canUnlock = true;
    }
    private void setPassive(boolean passive) {
        this.Passive = true;
    }

  //  public abstract void onItemUse(Player player, ItemStack itemStack);
}
