package yezi.abilityevolve.config;

import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillLockLoader {
    private static Map<String, Requirement[]> skillLocks = new HashMap<>();
    private static Map<String, Requirement[]> craftSkillLocks = new HashMap<>();
    private static Map<String, Requirement[]> attackSkillLocks = new HashMap<>();
    private static Map<String, String> skillAliasMap = new HashMap<>();

    public static void load() {
        loadSkillAliases();
        skillLocks.putAll(parseSkillLocks(JsonConfigLoader.loadJsonConfig("AbilityEvolve/skill_locks.json", "{}").get("skillLocks")));
        craftSkillLocks.putAll(parseSkillLocks(JsonConfigLoader.loadJsonConfig("AbilityEvolve/craft_skill_locks.json", "{}").get("craftSkillLocks")));
        attackSkillLocks.putAll(parseSkillLocks(JsonConfigLoader.loadJsonConfig("AbilityEvolve/attack_skill_locks.json", "{}").get("attackSkillLocks")));
    }

    private static void loadSkillAliases() {
        skillAliasMap.clear();
        for (String alias : ConfigManager.SKILL_ALIAS.get()) {
            String[] parts = alias.split("=");
            if (parts.length == 2) {
                skillAliasMap.put(parts[0].toLowerCase(), parts[1].toLowerCase());
            }
        }
    }

    private static Map<String, Requirement[]> parseSkillLocks(Map<String, List<String>> data) {
        Map<String, Requirement[]> locks = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            List<Requirement> requirements = new ArrayList<>();

            for (String raw : entry.getValue()) {
                String[] parts = raw.split(":");
                if (parts.length == 2) {
                    String skillName = parts[0].toLowerCase();
                    skillName = skillAliasMap.getOrDefault(skillName, skillName); // 替换别名
                    int level = Integer.parseInt(parts[1]);
                    requirements.add(new Requirement(Skill.valueOf(skillName.toUpperCase()).index, level));
                }
            }
            locks.put(entry.getKey(), requirements.toArray(new Requirement[0]));
        }
        return locks;
    }

    public static Requirement[] getRequirements(ResourceLocation key) {
        return skillLocks.getOrDefault(key.toString(), new Requirement[0]);
    }

    public static Requirement[] getCraftRequirements(ResourceLocation key) {
        return craftSkillLocks.getOrDefault(key.toString(), new Requirement[0]);
    }

    public static Requirement[] getAttackRequirements(ResourceLocation key) {
        return attackSkillLocks.getOrDefault(key.toString(), new Requirement[0]);
    }
    public static Map<String, Requirement[]> getSkillLocks() {
        return skillLocks;
    }
    public static Map<String, Requirement[]> getCraftSkillLocks() {
        return craftSkillLocks;
    }
    public static Map<String, Requirement[]> getAttackSkillLocks() {
        return attackSkillLocks;
    }
    public static Type getSkillLocksType() {
        return (new TypeToken<Map<String, List<String>>>() {
        }).getType();
    }

    public static void setSkillLocks(Map<String, Requirement[]> newSkillLocks) {
        skillLocks = newSkillLocks;
    }
    public static void setAttackSkillLocks(Map<String, Requirement[]> newAttackSkillLocks) {
        attackSkillLocks = newAttackSkillLocks;
    }
    public static void setCraftSkillLocks(Map<String, Requirement[]> newCraftSkillLocks) {
        craftSkillLocks = newCraftSkillLocks;
    }
}
