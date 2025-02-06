package yezi.skillablereforged.common.abilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.effects.StunEffect;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExposeWeaknessAbility extends Ability{
    private static final String name = "expose_weakness";
    private static final String description = "Continuing attack and suppress your target.";
    private static final int requirement = 22;
    GetAbilityLevel getAbilityLevel = new GetAbilityLevel();

    public int abilityLevel = getAbilityLevel.getAbilityLevelAttack1(SkillModel.get().getSkillLevel(Skill.ATTACK), requirement);
    public ExposeWeaknessAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.ATTACK, requirement
                        ),
                },
                "attack",
                3,
                8,
                true
        );
    }
    private static final Map<UUID, Integer> weaknessMarks = new HashMap<>();
    private static final Map<Integer, Integer> MARK_LIMIT = Map.of(1, 3, 2, 3, 3, 4, 4, 4, 5, 5);
    private static final Map<Integer, Double> DAMAGE_MULT = Map.of(1, 1.2, 2, 1.3, 3, 1.4, 4, 1.5, 5, 1.6);
    private static final Map<Integer, Integer> STUN_DURATION = Map.of(1, 20, 2, 25, 3, 30, 4, 35, 5, 40);
   // private final int abilityLevel = getAbilityLevel.getAbilityLevelAttack1(SkillModel.get().getSkillLevel(Skill.ATTACK), requirement);
   public void applyWeaknessMark(LivingEntity target, Player attacker, float damage) {
       UUID targetId = target.getUUID();
       int currentMarks = weaknessMarks.getOrDefault(targetId, 0) + 1;
       weaknessMarks.put(targetId, currentMarks);
       int threshold = MARK_LIMIT.getOrDefault(abilityLevel, 3);
       if (currentMarks >= threshold) {
           double damageMultiplier = DAMAGE_MULT.getOrDefault(abilityLevel, 1.2);
           double stunDuration = STUN_DURATION.getOrDefault(abilityLevel, 20) / 20.0;
           target.hurt(target.damageSources().playerAttack(attacker), (float) (damage * damageMultiplier));
           StunEffect.apply(target, stunDuration);
           weaknessMarks.remove(targetId);
           attacker.causeFoodExhaustion(1.0f);
       }
   }
}
