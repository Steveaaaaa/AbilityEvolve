package yezi.skillablereforged.common.skills;

public enum Skill {
    GRAZIERY(0, "skill.graziery"),
    MINING(1, "skill.mining"),
    GATHERING(2, "skill.gathering"),
    ATTACK(3, "skill.attack"),
    DEFENSE(4, "skill.defense"),
    BUILDING(5, "skill.building"),
    FARMING(6, "skill.farming"),
    AGILITY(7, "skill.agility"),
    MAGIC(8, "skill.magic"),
    SHOOTING(9, "skill.shooting");

    public final int index;
    public final String displayName;

    Skill(int index, String name) {
        this.index = index;
        this.displayName = name;
    }

    public int getIconIndex() {
        return this.index;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
