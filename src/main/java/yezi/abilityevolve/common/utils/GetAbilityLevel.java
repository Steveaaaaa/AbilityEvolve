package yezi.abilityevolve.common.utils;

public class GetAbilityLevel {
    public static int getAbilityLevelGraziery1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
    public static int getAbilityLevelGraziery3(int skillLevel, int requirement){
        return Math.min(Math.max(1 ,1+(skillLevel -requirement)/3), 10);
    }
    public static int getAbilityLevelMining2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/2), 5);
    }
    public static int getAbilityLevelMining3(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/3), 5);
    }
    public static int getAbilityLevelGathering2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1+(skillLevel -requirement)/2), 3);
    }
    public static int getAbilityLevelAttack1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
    public static int getAbilityLevelAttack2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + (skillLevel - requirement)/2), 10);
    }
    public static int getAbilityLevelDefense1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
    public static int getAbilityLevelDefense2(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + (skillLevel - requirement)/2), 6);
    }
    public static int getAbilityLevelDefense3(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + (skillLevel - requirement)/3), 5);
    }
    public static int getAbilityLevelDefense4(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + (skillLevel - requirement)), 5);
    }
    public static int getAbilityLevelBuilding1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
    public static int getAbilityLevelFarming1(int skillLevel, int requirement){
        return Math.min(Math.max(1, 1 + skillLevel - requirement), 10);
    }
}
