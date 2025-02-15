package yezi.abilityevolve.common.abilities;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class WolvesAbility extends Ability{

    private static final String name = "wolves";
    private static final String description = "Strengthen your wolves.";
    private static final int requirementGraziery = 20;
    private static final int DURATION_TICKS = 200;
    private static final int[] ATTACK_BONUS = {50, 56, 65, 77, 92, 110, 131, 155, 182, 225};
    public static final int[] DODGE_PERCENT = {35, 40, 45, 50, 55, 60, 65, 70, 75, 80};
    private static final int[] COOLDOWN_TICKS = {72, 71, 70, 69, 68, 67, 66, 65, 64, 64};

    private static final HashMap<UUID, Long> cooldownMap = new HashMap<>();
    public WolvesAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.GRAZIERY.index, requirementGraziery
                        ),
                        new Requirement(
                                Skill.ATTACK.index, 12
                        )
                },
                "graziery",
                3,
                8,
                true
        );
    }
    private static final UUID ATTACK_BONUS_UUID = UUID.fromString("e62b68fc-0b1c-4a49-9f23-984d8f4e1e77");
    public void applyEffect(Wolf wolf, Player player) {
        if (wolf == null) return;
    //    int abilityLevel = getAbilityLevel();
        int abilityLevel = GetAbilityLevel.getAbilityLevelGraziery1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.GRAZIERY),requirementGraziery);
        UUID wolfId = wolf.getUUID();
        long currentTime = System.currentTimeMillis();

        if (cooldownMap.containsKey(wolfId) && (currentTime - cooldownMap.get(wolfId) < COOLDOWN_TICKS[abilityLevel-1] * 1000)) {
            return;
        }
        cooldownMap.put(wolfId, currentTime);

        double attackBonusPercent = ATTACK_BONUS[abilityLevel-1] / 100.0;
        AttributeInstance attackAttr = wolf.getAttribute(Attributes.ATTACK_DAMAGE);

        if (attackAttr != null) {
            attackAttr.removeModifier(ATTACK_BONUS_UUID);
            double baseAttack = attackAttr.getBaseValue();
            double bonusAttack = baseAttack * attackBonusPercent;
            AttributeModifier attackModifier = new AttributeModifier(ATTACK_BONUS_UUID, "Wolf Attack Bonus", bonusAttack, AttributeModifier.Operation.ADDITION);
            attackAttr.addTransientModifier(attackModifier);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (attackAttr != null) {
                    attackAttr.removeModifier(ATTACK_BONUS_UUID);
                }
            }
        }, DURATION_TICKS * 50L);
    }
    public int getAbilityLevel(Player player) {
        return GetAbilityLevel.getAbilityLevelGraziery1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.GRAZIERY),requirementGraziery);
    }
}
