package yezi.skillablereforged.common.skills;

import net.minecraft.resources.ResourceLocation;
import yezi.skillablereforged.Config;

import java.util.function.Function;

public enum RequirementType {
    USE(Config::getRequirements),
    CRAFT(Config::getCraftRequirements),
    ATTACK(Config::getEntityAttackRequirements);
  //  ABILITY(Config::getAbilityRequirements);

    private final Function<ResourceLocation, Requirement[]> requirementMap;

    RequirementType(Function<ResourceLocation, Requirement[]> requirementMap) {
        this.requirementMap = requirementMap;
    }

    // 获取要求的方法
    public Requirement[] getRequirements(ResourceLocation resource) {
        // 返回对应的 Requirement[]
        return this.requirementMap.apply(resource);
    }
    public Requirement[] getRequirements(String name) {
        return getRequirements(new ResourceLocation(name));
    }
}