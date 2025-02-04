package yezi.skillablereforged.common.abilities;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.utils.GetAbilityLevel;

import java.util.UUID;

public class IronCavalryAbility extends Ability {
    private static final String name = "iron_cavalry";
    private static final String description = "When you ride, increase your damage.";
    private static final int requirementGraziery = 16;
    private static final int[] ATTACK_BONUS = {25, 30, 35, 40, 45, 50, 55, 60, 65, 70};
    private static final int[] ARMOR_BONUS = {30,35,40,45,50,60,75,80,100};
    GetAbilityLevel getAbilityLevel = new GetAbilityLevel();
    public IronCavalryAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.GRAZIERY, requirementGraziery
                        ),
                        new Requirement(
                                Skill.ATTACK, 10
                        )
                },
                "graziery",
                2,
                6,
                true
        );
    }

    private static final UUID ATTACK_BONUS_UUID = UUID.fromString("d1b4ec21-cfa4-49d4-b7e3-d6937bb9f1b9");

    int abilityLevel = getAbilityLevel.getAbilityLevel(SkillModel.get().getSkillLevel(Skill.GRAZIERY), requirementGraziery+1);
    public void applyAttackBonus(Player player){
        AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null) {
            double baseAttack = attackAttr.getBaseValue();
            double bonusAttack = baseAttack * (ATTACK_BONUS[abilityLevel] / 100.0);
            AttributeModifier attackModifier = new AttributeModifier(ATTACK_BONUS_UUID, "Iron Cavalry Attack Bonus", bonusAttack, AttributeModifier.Operation.ADDITION);
            attackAttr.addTransientModifier(attackModifier);
        }
    }
    public void applyDefenseBonus(Player player, Pig pig){
        double playerArmor = player.getArmorValue();
        double armorBonus = playerArmor * (ARMOR_BONUS[abilityLevel] / 100.0);
        AttributeInstance pigArmorAttr = pig.getAttribute(Attributes.ARMOR);
        if (pigArmorAttr != null) {
            double baseArmor = pigArmorAttr.getBaseValue();
            double bonusArmor = baseArmor + armorBonus;
            pigArmorAttr.setBaseValue(bonusArmor);
        }
    }
}
