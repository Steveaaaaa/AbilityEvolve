package yezi.skillablereforged.common.commands.abilities;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.world.entity.player.Player;
import yezi.skillablereforged.common.capabilities.AbilityModel;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;

public class AbilityManager {
    private final AbilityModel abilityModel;

    public AbilityManager(Player player) {
        this.abilityModel = AbilityModel.get(player);
    }

    public boolean checkForGetAbility(String abilityType, int index, int skillPointCost) {
        int[] abilityGroup = getAbilityGroupBySkill(abilityType);
        return abilityGroup != null && abilityGroup[index] == 0 && skillPointCost <= abilityModel.abilityPoint - abilityModel.abilityPointCosts;
    }

    private int[] getAbilityGroupBySkill(String skillName) {
        return switch (skillName.toLowerCase()) {
            case "graziery" -> abilityModel.grazieryLock;
            case "mining" -> abilityModel.miningLock;
            case "gathering" -> abilityModel.gatheringLock;
            case "attack" -> abilityModel.attackLock;
            case "defense" -> abilityModel.defenseLock;
            case "building" -> abilityModel.buildingLock;
            case "farming" -> abilityModel.farmingLock;
            case "agility" -> abilityModel.agilityLock;
            case "magic" -> abilityModel.magicLock;
            case "shooting" -> abilityModel.shootingLock;
            default -> null;
        };
    }
    public void getAbility(String abilityType, int index,int skillPointCost) {
        if (checkForGetAbility(abilityType, index, skillPointCost)){
            switch (abilityType.toLowerCase()) {
                case "graziery" -> abilityModel.grazieryLock[index] = 1;
                case "mining" -> abilityModel.miningLock[index] = 1;
                case "gathering" -> abilityModel.gatheringLock[index] = 1;
                case "attack" -> abilityModel.attackLock[index] = 1;
                case "defense" -> abilityModel.defenseLock[index] = 1;
                case "building" ->abilityModel.buildingLock[index] = 1;
                case "farming" -> abilityModel.farmingLock[index] = 1;
                case "agility" -> abilityModel.agilityLock[index] = 1;
                case "magic" -> abilityModel.magicLock[index] = 1;
                case "shooting" -> abilityModel.shootingLock[index] = 1;
                default -> LOGGER.error("Invalid ability type: {}", abilityType);
            }
            abilityModel.abilityPointCosts += skillPointCost;
        }
    }
    public List<Pair<String, Integer>> getUnlockedPassiveAbilities() {
        List<Pair<String, Integer>> unlockedAbilities = new ArrayList<>();

        checkUnlockedAbilities(abilityModel.grazieryLock, "graziery", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.miningLock, "mining", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.gatheringLock, "gathering", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.attackLock, "attack", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.defenseLock, "defense", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.buildingLock, "building", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.farmingLock, "farming", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.agilityLock, "agility", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.magicLock, "magic", unlockedAbilities);
        checkUnlockedAbilities(abilityModel.shootingLock, "shooting", unlockedAbilities);

        return unlockedAbilities;
    }
    private void checkUnlockedAbilities(int[] abilityGroup, String abilityType, List<Pair<String, Integer>> list) {
        for (int i = 0; i < abilityGroup.length; i++) {
            if (abilityGroup[i] == 1) {
                list.add(Pair.of(abilityType, i));
            }
        }
    }
}
