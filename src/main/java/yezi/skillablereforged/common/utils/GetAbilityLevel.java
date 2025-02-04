package yezi.skillablereforged.common.utils;

public class GetAbilityLevel {
    int skillLevel;
    int requirement;
    public int getAbilityLevelGraziery3(int skillLevel, int requirement){
        return Math.min(Math.max(1 ,skillLevel - requirement - 1), 10);
    }
    public int getAbilityLevelMining3(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/3), 5);
    }
}
