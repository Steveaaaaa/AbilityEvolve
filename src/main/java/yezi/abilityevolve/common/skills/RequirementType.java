package yezi.abilityevolve.common.skills;

import net.minecraft.resources.ResourceLocation;
import yezi.abilityevolve.config.SkillLockLoader;

import java.util.function.Function;

public enum RequirementType {
    USE(SkillLockLoader::getRequirements),
    CRAFT(SkillLockLoader::getCraftRequirements),
    ATTACK(SkillLockLoader::getAttackRequirements);

    private final Function<ResourceLocation, Requirement[]> requirementMap;

    RequirementType(Function<ResourceLocation, Requirement[]> requirementMap) {
        this.requirementMap = requirementMap;
    }

    public Requirement[] getRequirements(ResourceLocation resource) {
        return this.requirementMap.apply(resource);
    }
}