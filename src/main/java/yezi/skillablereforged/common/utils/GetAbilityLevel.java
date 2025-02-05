package yezi.skillablereforged.common.utils;

public class GetAbilityLevel {
    public int getAbilityLevelGraziery1(int skillLevel, int requirement){
        return Math.min(Math.max(1, skillLevel+1 - requirement), 10);
    }
    public int getAbilityLevelGraziery3(int skillLevel, int requirement){
        return Math.min(Math.max(1 ,skillLevel - requirement - 1), 10);
    }
    public int getAbilityLevelMining2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/2), 5);
    }
    public int getAbilityLevelMining3(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/3), 5);
    }
}
