package yezi.abilityevolve.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigManager {
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final ForgeConfigSpec.BooleanValue DISABLE_WOOL;
    public static final ForgeConfigSpec.BooleanValue DEATH_RESET;
    public static final ForgeConfigSpec.IntValue STARTING_COST;
    public static final ForgeConfigSpec.IntValue COST_INCREASE;
    public static final ForgeConfigSpec.IntValue MAXIMUM_LEVEL;
    public static final ForgeConfigSpec.IntValue ABILITY_POINT_INCREASE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SKILL_ALIAS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Disable wool drops to force the player to get shears.");
        DISABLE_WOOL = builder.define("disableWoolDrops", true);

        builder.comment("Reset all skills to 1 when a player dies.");
        DEATH_RESET = builder.define("deathSkillReset", false);

        builder.comment("Starting cost of upgrading to level 2, in levels.");
        STARTING_COST = builder.defineInRange("startingCost", 2, 0, 10);

        builder.comment("Amount of levels added to the cost with each upgrade.");
        COST_INCREASE = builder.defineInRange("costIncrease", 1, 0, 10);

        builder.comment("Maximum level each skill can be upgraded to.");
        MAXIMUM_LEVEL = builder.defineInRange("maximumLevel", 32, 2, 100);

        builder.comment("Skills can grant one skill point every X levels.");
        ABILITY_POINT_INCREASE = builder.defineInRange("abilityPointIncrease", 2, 1, 32);
        builder.comment(
                "List of substitutions to perform in names in skill lock lists.",
                "Useful if you're using a resource pack to change the names of skills, this config doesn't affect gameplay, just accepted values in other configs so it's easier to think about",
                "Format: key=value", "Valid values: attack, defense, mining, gathering, farming, building, agility, magic");
        builder.comment("List of skill name substitutions.");
        SKILL_ALIAS = builder.defineList("skillAliases", List.of("defense=defense"), obj -> obj instanceof String);

        CONFIG_SPEC = builder.build();
    }
    public static int getMaxLevel() {
        return MAXIMUM_LEVEL.get();
    }
    public static int getAbilityPointIncrease() {
        return ABILITY_POINT_INCREASE.get();
    }
    public static int getStartCost() {
        return STARTING_COST.get();
    }
    public static int getCostIncrease() {
        return COST_INCREASE.get();
    }
    public static boolean getDisableWool() {
        return DISABLE_WOOL.get();
    }
    public static boolean getDeathReset() {
        return DEATH_RESET.get();
    }

}

