package yezi.abilityevolve.common.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.UUID;

public class RapidStabAbility extends Ability{
    private static final String name = "rapid_stab";
    private static final String description = "Attack speed increases, attack power decreases.";
    private static final int requirement = 12;

    public int abilityLevel = GetAbilityLevel.getAbilityLevelAttack1(ModCapabilities.getSkillModel(Minecraft.getInstance().player).getSkillLevel(Skill.ATTACK), requirement);
    public RapidStabAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.ATTACK.index, requirement
                        ),
                },
                "attack",
                0,
                4,
                true
        );
    }
    private static final double[] ATTACK_SPEED_BONUS = {10, 20, 30, 40, 50, 56, 62, 68, 74, 80};
    private static final double[] ATTACK_DAMAGE_REDUCTION = {8, 16, 24, 32, 38, 35, 33, 30, 27, 24};

    public void applyEffect(ServerPlayer player) {
        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);

        if (attackSpeed != null && !attackSpeed.hasModifier(getAttackSpeedModifier())) {
            attackSpeed.addTransientModifier(getAttackSpeedModifier());
        }
        if (attackDamage != null && !attackDamage.hasModifier(getAttackDamageModifier())) {
            attackDamage.addTransientModifier(getAttackDamageModifier());
        }
    }
    private AttributeModifier getAttackSpeedModifier() {
        return new AttributeModifier(UUID.nameUUIDFromBytes("RapidStabSpeed".getBytes()),
                "RapidStabSpeed", ATTACK_SPEED_BONUS[abilityLevel-1] / 100.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    private AttributeModifier getAttackDamageModifier() {
        return new AttributeModifier(UUID.nameUUIDFromBytes("RapidStabDamage".getBytes()),
                "RapidStabDamage", -ATTACK_DAMAGE_REDUCTION[abilityLevel-1] / 100.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}

