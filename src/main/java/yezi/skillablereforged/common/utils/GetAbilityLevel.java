package yezi.skillablereforged.common.utils;

public class GetAbilityLevel {
    int skillLevel;
    int requirement;
    public int getAbilityLevel(int skillLevel, int requirement){
        if (skillLevel > requirement) {
            return skillLevel - requirement;
        } else {
            return 1;
        }
    }
}
