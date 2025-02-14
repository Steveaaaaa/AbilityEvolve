package yezi.abilityevolve.common.abilities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConcealAbility extends Ability{
    private static final String name = "conceal";
    private static final String description = "Become a assassin";
    public static final int requirement = 18;
    public ConcealAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.AGILITY.index, requirement),
                        new Requirement(Skill.ATTACK.index, 6)
                },
                "agility",
                2,
                8,
                true
        );
    }
    private static final Map<UUID, Long> LAST_ACTION_TIMES = new HashMap<>();
    private static final Map<UUID, Boolean> STEALTH_MARKS = new HashMap<>();
    private static final int[] X_VALUES = {12, 12, 12, 12, 11, 11, 11, 10, 10, 9};
    private static final int[] Y_VALUES = {200, 215, 230, 245, 245, 260, 275, 275, 290, 300};

    public static void updateActionTime(Player player) {

        LAST_ACTION_TIMES.put(player.getUUID(), player.level().getGameTime());
        if (isStealthMarked(player)) {
            removeStealth(player);
        }
    }
    public static void checkAndApplyStealth(Player player) {
        if (player.level().isClientSide) return;

       // int level = PLAYER_LEVELS.get(player);
        long currentTime = player.level().getGameTime();
        long lastAction = LAST_ACTION_TIMES.getOrDefault(player.getUUID(), currentTime);
        long requiredTicks = getRequiredTicks(GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.AGILITY), requirement));

        if (currentTime - lastAction >= requiredTicks && !isStealthMarked(player)) {
            applyStealth(player);
        }
    }

    public static float handleAttack(Player player) {
        if (!isStealthMarked(player)) return 1.0f;

        float multiplier = getDamageMultiplier(GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.AGILITY), requirement));
        removeStealth(player);
        return multiplier;
    }

    private static int getRequiredTicks(int level) {
        return  20 * X_VALUES[level-1];
    }

    private static float getDamageMultiplier(int level) {
        return Y_VALUES[level-1];
    }

    private static boolean isStealthMarked(Player player) {
        return STEALTH_MARKS.containsKey(player.getUUID());
    }

    private static void applyStealth(Player player) {
        STEALTH_MARKS.put(player.getUUID(), true);
        player.addEffect(new MobEffectInstance(
                MobEffects.INVISIBILITY,
                Integer.MAX_VALUE, // 永久持续直到移除
                0,
                false,
                false
        ));
    }

    private static void removeStealth(Player player) {
        STEALTH_MARKS.remove(player.getUUID());
        player.removeEffect(MobEffects.INVISIBILITY);
    }

    public static void cleanupPlayer(Player player) {
        LAST_ACTION_TIMES.remove(player.getUUID());
        STEALTH_MARKS.remove(player.getUUID());
    }
}
