package yezi.skillablereforged.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import static com.mojang.text2speech.Narrator.LOGGER;

public class AbilityModel {
    public int[] grazieryLock = new int[]{0,0,0,0};
    public int[] miningLock = new int[]{0,0,0};
    public int[] gatheringLock = new int[]{0,0,0};
    public int[] attackLock = new int[]{0,0,0,0};
    public int[] defenseLock = new int[]{0,0,0,0};
    public int[] buildingLock = new int[]{0,0,0};
    public int[] farmingLock = new int[]{0,0,0};
    public int[] agilityLock = new int[]{0,0,0,0};
    public int[] magicLock = new int[]{0,0,0,0};
    public int[] shootingLock = new int[]{0,0,0,0};
    public int abilityPoint = 0;
    public int abilityPointCosts = 0;
    public AbilityModel() {
    }
    public static AbilityModel get() {
        return new AbilityModel();
    }
    public static AbilityModel get(Player player) {
        LazyOptional<AbilityModel> abilityModelOptional = player.getCapability(AbilityCapability.INSTANCE);

        return abilityModelOptional.orElseGet(() -> {
            LOGGER.warn("Player " + player.getName().getString() + " does not have an Ability Model, creating default.");
            return new AbilityModel();
        });
    }
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("grazieryLock", this.grazieryLock);
        tag.putIntArray("miningLock", this.miningLock);
        tag.putIntArray("gatheringLock", this.gatheringLock);
        tag.putIntArray("attackLock", this.attackLock);
        tag.putIntArray("defenseLock", this.defenseLock);
        tag.putIntArray("buildingLock", this.buildingLock);
        tag.putIntArray("farmingLock", this.farmingLock);
        tag.putIntArray("agilityLock", this.agilityLock);
        tag.putIntArray("magicLock", this.magicLock);
        tag.putIntArray("shootingLock", this.shootingLock);
        tag.putInt("abilityPoint", this.abilityPoint);
        tag.putInt("abilityPointCosts", this.abilityPointCosts);
        return tag;
    }
    public void deserializeNBT(CompoundTag nbt) {
        this.grazieryLock = nbt.getIntArray("grazieryLock");
        this.miningLock = nbt.getIntArray("miningLock");
        this.gatheringLock = nbt.getIntArray("gatheringLock");
        this.attackLock = nbt.getIntArray("attackLock");
        this.defenseLock = nbt.getIntArray("defenseLock");
        this.buildingLock = nbt.getIntArray("buildingLock");
        this.farmingLock = nbt.getIntArray("farmingLock");
        this.agilityLock = nbt.getIntArray("agilityLock");
        this.magicLock = nbt.getIntArray("magicLock");
        this.shootingLock = nbt.getIntArray("shootingLock");
        this.abilityPoint = nbt.getInt("abilityPoint");
        this.abilityPointCosts = nbt.getInt("abilityPointCost");
    }
    public int getAbilitySkillIndex(String abilityType){
        return switch (abilityType) {
            case "graziery" -> 0;
            case "mining" -> 1;
            case "gathering" -> 2;
            case "attack" -> 3;
            case "defense" -> 4;
            case "building" -> 5;
            case "farming" -> 6;
            case "agility" -> 7;
            case "magic" -> 8;
            case "shooting" -> 9;
            default -> -1;
        };
    }
}
