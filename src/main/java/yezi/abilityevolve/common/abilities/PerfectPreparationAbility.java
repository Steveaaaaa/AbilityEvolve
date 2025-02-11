package yezi.abilityevolve.common.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PerfectPreparationAbility extends Ability {
    private static final String name = "perfect_preparation";
    private static final String description = "Gain one-time death immunity with buffs.";
    private static final int requirement = 16;

    private static final Map<UUID, Long> lastUsedTime = new HashMap<>();
    private static final Map<UUID, Boolean> invinciblePlayers = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final float[] ASORB_PERCENTAGE = {0.0f, 0.05f, 0.1f, 0.15f, 0.25f};
    private static final int[] INVINCIBILITY_DURATION = {3, 4, 5, 6, 6};

    public static int abilityLevel = GetAbilityLevel.getAbilityLevelDefense3(ModCapabilities.getSkillModel(Minecraft.getInstance().player).getSkillLevel(Skill.DEFENSE), requirement);

    public PerfectPreparationAbility() {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.DEFENSE.index, requirement)
                },
                "defense",
                1,
                6,
                true
        );
    }

    public static void triggerAbility(Player player) {
        UUID playerUUID = player.getUUID();
        Level world = player.level();

        long currentDay = world.getGameTime() / 24000;
        long lastDayUsed = lastUsedTime.getOrDefault(playerUUID, -1L);

        if (currentDay == lastDayUsed) {
            return;
        }

        lastUsedTime.put(playerUUID, currentDay);

        player.removeAllEffects();

        float maxHealth = player.getMaxHealth();
        float absorptionAmount = maxHealth * getAbsorptionPercentage();

        player.setAbsorptionAmount(absorptionAmount);

        player.setHealth(player.getMaxHealth() * 0.5f);

        int invincibilityDuration = getInvincibilityDuration();
        invinciblePlayers.put(playerUUID, true);

        if (world instanceof ServerLevel serverLevel) {
            serverLevel.getServer().execute(() -> {
                scheduler.schedule(() -> {
                    invinciblePlayers.remove(playerUUID);
                }, invincibilityDuration, TimeUnit.SECONDS);
            });
        }
    }

    public static boolean isInvincible(Player player) {
        return invinciblePlayers.getOrDefault(player.getUUID(), false);
    }

    private static float getAbsorptionPercentage() {
        return ASORB_PERCENTAGE[abilityLevel - 1];
    }

    private static int getInvincibilityDuration() {
        return INVINCIBILITY_DURATION[abilityLevel - 1];
    }
}
