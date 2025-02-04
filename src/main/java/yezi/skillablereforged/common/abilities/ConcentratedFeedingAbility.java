package yezi.skillablereforged.common.abilities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.Skill;

import java.util.Map;

public class ConcentratedFeedingAbility extends Ability{
    private static final int JUMP_BOOST_LEVEL = 2;
    private static final int SPEED_LEVEL = 3;
    private static final int requirementGraziery = 8;
    private static final Map<Integer, Double[]> map = Map.of(
            1, new Double[]{0.21250, 0.66667},
            2, new Double[]{0.21989, 0.68636},
            3, new Double[]{0.22762, 0.70698},
            4, new Double[]{0.23571, 0.72857},
            5, new Double[]{0.24421, 0.75122},
            6, new Double[]{0.25673, 0.78462},
            7, new Double[]{0.26645, 0.81053},
            8, new Double[]{0.27669, 0.83784},
            9, new Double[]{0.28750, 0.86667},
            10, new Double[]{0.30536, 0.91429}
    );
    public ConcentratedFeedingAbility() {
        super(
                "concentrated_feeding",
                "Strengthen the horse you ride.",
                new Requirement[]{new Requirement(Skill.GRAZIERY, requirementGraziery),new Requirement(Skill.AGILITY,4)},
                "graziery", 1, 4, true
        );
    }
    int skillLevel = SkillModel.get().getSkillLevel(Skill.GRAZIERY);
    public Double[] getStandard(){
        if (skillLevel > requirementGraziery){
            abilityLevel = skillLevel - requirementGraziery;
        }else abilityLevel = 1;
        return map.get(abilityLevel);
    }

    public void applyMountEffects(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, SPEED_LEVEL, false, false, true)); // 速度 3
        entity.addEffect(new MobEffectInstance(MobEffects.JUMP, Integer.MAX_VALUE, JUMP_BOOST_LEVEL, false, false, true)); // 跳跃提升 2
    }
    public void removeMountEffects(LivingEntity entity) {
        entity.removeEffect(MobEffects.MOVEMENT_SPEED);
        entity.removeEffect(MobEffects.JUMP);
    }
}
