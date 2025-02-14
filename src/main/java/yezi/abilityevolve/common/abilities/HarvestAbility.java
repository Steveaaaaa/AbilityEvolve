package yezi.abilityevolve.common.abilities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

public class HarvestAbility extends Ability{
    private static final String name = "harvest";
    private static final String description = "";
    public static final int requirement = 16;
    public HarvestAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.FARMING.index, requirement
                        ),
                        new Requirement(
                                Skill.ATTACK.index, 16
                        )
                },
                "farming",
                1,
                8,
                true
        );
    }
    private static final int[] X_VALUES = {20, 25, 30, 35, 40, 45, 50, 55, 60, 65};
    public static float calculateExtraDamage(int abilityLevel, Player player) {
        if (abilityLevel < 1 || abilityLevel > 10) return 0;

        FoodData foodData = player.getFoodData();
        float totalHunger = foodData.getFoodLevel() + foodData.getSaturationLevel();
        int maxX = X_VALUES[abilityLevel - 1]; 

        if (totalHunger <= 0) return 0;

        float effectiveHunger = Math.min(totalHunger, maxX);
        return abilityLevel * effectiveHunger / 40.0f;
    }
    public static void applyHungerEffect(Player player) {
        MobEffectInstance currentEffect = player.getEffect(MobEffects.HUNGER);
        int newAmplifier = (currentEffect != null) ? currentEffect.getAmplifier() + 1 : 0;
        int newDuration = 40;

        if (currentEffect != null) {
            newDuration = Math.max(currentEffect.getDuration(), newDuration);
        }

        newAmplifier = Math.max(newAmplifier, 2);

        player.addEffect(new MobEffectInstance(
                MobEffects.HUNGER,
                newDuration,
                newAmplifier,
                true,
                true
        ));
    }
}
