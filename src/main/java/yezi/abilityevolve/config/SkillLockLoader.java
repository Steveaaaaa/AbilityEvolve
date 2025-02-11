package yezi.abilityevolve.config;

import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import yezi.abilityevolve.AbilityEvolve;
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
    private static final Map<String, String> skillAliasMap = new HashMap<>();

    public static void load() {
        loadSkillAliases();

        Map<String, List<String>> skillLocksData = JsonConfigLoader.loadJsonConfig("AbilityEvolve/skill_locks.json",
                "{\n" +
                "  \"skillLocks\": {\n" +
                "    \"minecraft:diamond\": [\n" +
                "      \"attack:5\",\n" +
                "      \"defense:3\"\n" +
                "    ],\n" +
                "    \"minecraft:torch\": [\n" +
                "      \"crafting:2\",\n" +
                "      \"magic:4\"\n" +
                "    ]\n" +
                "  }\n" +
                "}").get("skillLocks");
        Map<String, List<String>> craftSkillLocksData = JsonConfigLoader.loadJsonConfig("AbilityEvolve/craft_skill_locks.json",
                "{\n" +
                "  \"craftSkillLocks\": {\n" +
                "    \"minecraft:crafting_table\": [\n" +
                "      \"crafting:3\",\n" +
                "      \"smithing:2\"\n" +
                "    ]\n" +
                "  }\n" +
                "}").get("craftSkillLocks");
        AbilityEvolve.LOGGER.info("Loaded skill locks: {}", craftSkillLocks);
        Map<String, List<String>> attackSkillLocksData = JsonConfigLoader.loadJsonConfig("AbilityEvolve/attack_skill_locks.json",
                "{\n" +
                "  \"attackSkillLocks\": {\n" +
                "    \"minecraft:bow\": [\n" +
                "      \"archery:5\",\n" +
                "      \"attack:4\"\n" +
                "    ]\n" +
                "  }\n" +
                "}").get("attackSkillLocks");

        skillLocks.putAll(parseSkillLocks(skillLocksData != null ? skillLocksData : new HashMap<>()));
        craftSkillLocks.putAll(parseSkillLocks(craftSkillLocksData != null ? craftSkillLocksData : new HashMap<>()));
        attackSkillLocks.putAll(parseSkillLocks(attackSkillLocksData != null ? attackSkillLocksData : new HashMap<>()));
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

        if (data == null || data.isEmpty()) {
            return locks;
        }

        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            List<Requirement> requirements = new ArrayList<>();

            for (String raw : entry.getValue()) {
                String[] parts = raw.split(":");
                if (parts.length == 2) {
                    String skillName = parts[0].toLowerCase();
                    skillName = skillAliasMap.getOrDefault(skillName, skillName);

                    try {
                        Requirement requirement = new Requirement(Skill.valueOf(skillName.toUpperCase()).index, Integer.parseInt(parts[1]));
                        requirements.add(requirement);
                    } catch (IllegalArgumentException e) {
                        AbilityEvolve.LOGGER.error("Invalid skill name: {}", skillName);
                    }
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
