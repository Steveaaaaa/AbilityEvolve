package yezi.abilityevolve.common.skills;

public class Requirement {
    public final int index;
    public final int level;

    public Requirement(int index, int level) {
        this.index = index;
        this.level = level;
    }

    public String toString() {
        return "Requirement{skill=" + this.index + ", level=" + this.level + "}";
    }
}