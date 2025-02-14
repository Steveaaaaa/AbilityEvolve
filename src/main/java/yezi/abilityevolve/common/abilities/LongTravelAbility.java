package yezi.abilityevolve.common.abilities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import yezi.abilityevolve.common.listener.PlayerTickListener;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

public class LongTravelAbility extends Ability{
    private static final String name = "long_travel";
    private static final String description = "Speed up when you are in adventure";
    public static final int requirement = 12;
    public LongTravelAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.AGILITY.index, requirement)
                },
                "agility",
                1,
                4,
                true
        );
    }
    private static final int[] X_VALUES = {283, 276, 268, 257, 247, 236, 222, 209, 195, 178};
    private static final int[] Y_VALUES = {-53, -51, -49, -44, -41, -38, -32, -28, -24, -17};
    public static int getXThreshold(int level) {
        return X_VALUES[level - 1];
    }
    public static int getYThreshold(int level) {
        return Y_VALUES[level - 1];
    }
    public static void applyHeightEffect(Player player, int level) {

        int currentY = (int) player.getY();
        int xThreshold = getXThreshold(level);
        int yThreshold = getYThreshold(level);

      //  boolean shouldApply = (currentY >= xThreshold) || (currentY <= yThreshold);
        if ((currentY >= xThreshold) || (currentY <= yThreshold))
            PlayerTickListener.longTravelUnlockedMap.put(player.getUUID(), true);
        else PlayerTickListener.longTravelUnlockedMap.put(player.getUUID(), false);

        if (PlayerTickListener.longTravelUnlockedMap.get(player.getUUID())) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED,
                    40,
                    0,
                    false,
                    false
            ));

        }
    }
}
