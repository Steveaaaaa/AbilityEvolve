package yezi.skillablereforged.common.utils;

public class GetAbilityLevel {
    public int getAbilityLevelGraziery1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
    public int getAbilityLevelGraziery3(int skillLevel, int requirement){
        return Math.min(Math.max(1 ,1+(skillLevel -requirement)/3), 10);
    }
    public int getAbilityLevelMining2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/2), 5);
    }
    public int getAbilityLevelMining3(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/3), 5);
    }
    public int getAbilityLevelGathering2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/2), 3);
    }
    public int getAbilityLevelAttack1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
    public int getAbilityLevelAttack2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + (skillLevel - requirement)/2), 10);
    }
}
