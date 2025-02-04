package yezi.skillablereforged.common.capabilities;

import net.minecraftforge.common.util.LazyOptional;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.RequirementType;
import yezi.skillablereforged.common.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;

import static com.mojang.text2speech.Narrator.LOGGER;

public class SkillModel implements INBTSerializable<CompoundTag> {
    public int[] skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    public int[] skillExperience = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public SkillModel() {
    }

    public int getSkillLevel(Skill skill) {
        return this.skillLevels[skill.index];
    }

    public void setSkillLevel(Skill skill, int level) {
        this.skillLevels[skill.index] = level;
    }

    public void increaseSkillLevel(Skill skill) {
        this.skillLevels[skill.index]++;
    }

    public void addExperience(Skill skill, int experience) {
        this.skillExperience[skill.index] += experience;
  //      var10000[var10001] += experience;
        this.checkForLevelUp(skill);
    }

    private void checkForLevelUp(Skill skill) {
        while(this.skillExperience[skill.index]>=this.experienceToNextLevel(this.skillLevels[skill.index])){
            this.skillExperience[skill.index] -= this.experienceToNextLevel(this.skillLevels[skill.index]);
            this.skillLevels[skill.index]++;
        }
    }

    private int experienceToNextLevel(int level) {
        return level * 100;
    }

    public boolean canUseItem(Player player, ItemStack item) {
        return this.canUse(player, item.getItem().builtInRegistryHolder().key().location());
    }

    public boolean canUseBlock(Player player, Block block) {
        return !this.canUse(player, block.builtInRegistryHolder().key().location());
    }

    public boolean canUseEntity(Player player, Entity entity) {
        return this.canUse(player, entity.getType().builtInRegistryHolder().key().location());
    }

    private boolean canUse(Player player, ResourceLocation resource) {
        return this.checkRequirements(player, resource, RequirementType.USE);
    }

    private boolean checkRequirements(Player player, ResourceLocation resource, RequirementType type) {
        Requirement[] requirements = type.getRequirements(resource);
        if (requirements != null) {

            for (Requirement requirement : requirements) {
                if (this.getSkillLevel(requirement.skill) < requirement.level) {
                    if (player instanceof ServerPlayer) {
                        String message = "You are not skilled enough to use this item.";
                        if (type == RequirementType.ATTACK) {
                            message = "You are not strong enough to attack this creature.";
                        }
                        player.sendSystemMessage(Component.literal(message));
                    }

                    return false;
                }
            }
        }

        return true;
    }

    public static SkillModel get(Player player) {
        LazyOptional<SkillModel> skillModelOptional = player.getCapability(SkillCapability.INSTANCE);

        if (!skillModelOptional.isPresent()) {
            LOGGER.warn("Player " + player.getName().getString() + " does not have a Skill Model, returning default model.");

            return new SkillModel();
        }

        return skillModelOptional.orElse(null);
    }

    public static SkillModel get() {
        assert Minecraft.getInstance().player != null;

        return Minecraft.getInstance().player.getCapability(SkillCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("Player does not have a Skill Model!"));
    }

    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putIntArray("skillLevels", this.skillLevels);
        compound.putIntArray("skillExperience", this.skillExperience);
        return compound;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.skillLevels = nbt.getIntArray("skillLevels");
        this.skillExperience = nbt.getIntArray("skillExperience");
    }

    public boolean canCraftItem(Player player, ItemStack stack) {
        ResourceLocation resource = stack.getItem().builtInRegistryHolder().key().location();
        return this.checkRequirements(player, resource, RequirementType.CRAFT);
    }

    public boolean canAttackEntity(Player player, Entity target) {
        ResourceLocation resource = target.getType().builtInRegistryHolder().key().location();
        return this.checkRequirements(player, resource, RequirementType.ATTACK);
    }
}