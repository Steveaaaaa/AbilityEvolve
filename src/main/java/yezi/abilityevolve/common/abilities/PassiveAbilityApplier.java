package yezi.abilityevolve.common.abilities;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.effects.StunEffect;
import yezi.abilityevolve.common.listener.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PassiveAbilityApplier {

    private final AbilityManager abilityManager;
    private final Map<String, Consumer<Integer>> abilityHandlers = new HashMap<>();
    private final Map<String, Boolean> abilityStates = new HashMap<>();

    public PassiveAbilityApplier(Player player) {
        this.abilityManager = new AbilityManager(player);
        initAbilityHandlers();
    }

    private void initAbilityHandlers() {
        abilityHandlers.put("graziery", this::applyGrazieryAbility);
        abilityHandlers.put("mining", this::applyMiningAbility);
        abilityHandlers.put("gathering", this::applyGatheringAbility);
        abilityHandlers.put("attack", this::applyAttackAbility);
        abilityHandlers.put("defense", this::applyDefenseAbility);
        abilityHandlers.put("building", this::applyBuildingAbility);
        abilityHandlers.put("farming", this::applyFarmingAbility);
        abilityHandlers.put("agility", this::applyAgilityAbility);
        abilityHandlers.put("magic", this::applyMagicAbility);
        abilityHandlers.put("shooting", this::applyShootingAbility);
    }

    public void applyUnlockedAbilities() {
        AbilityEvolve.LOGGER.info("应用被动能力:");
        List<Pair<String, Integer>> unlockedAbilities = abilityManager.getUnlockedPassiveAbilities();
        for (Pair<String, Integer> ability : unlockedAbilities) {
            abilityHandlers.getOrDefault(ability.getLeft(), index ->
                    AbilityEvolve.LOGGER.info("未知的被动能力: " + ability.getLeft())
            ).accept(ability.getRight());
        }
    }

    private void applyGrazieryAbility(int index) {
        switch (index) {
            case 0 -> applyAbility("isAidSupportAbilityActive", new AidSupportListener(), "援护生效");
            case 1 -> applyAbility("isRidingAbilityActive", new ConcentratedFeedingAbilityListener(), "精饲生效");
            case 2 -> applyAbility("isIronCavalryAbilityActive", new IronCavalryListener(), "铁骑生效");
            case 3 -> applyAbility("isWolvesAbilityActive", new WolvesAbilityListener(), "狼群生效");
        }
    }

    private void applyMiningAbility(int index) {
        switch (index) {
            case 0 -> applyAbility("isAssociatedOreAbilityActive", new AssociatedOreListener(), "伴生生效");
            case 1 -> applyAbility("isGreedyAbilityActive", new GreedyListener(), "贪婪生效");
            case 2 -> applyAbility("isExplosiveAbilityActive", new ExplosiveMiningListener(), "爆破掘进生效");
        }
    }

    private void applyGatheringAbility(int index) {
        if (index == 0) applyAbility("isPanningAbilityActive", new PanningListener(), "淘金生效");
    }

    private void applyAttackAbility(int index) {
        switch (index) {
            case 0 -> applyAbility("isRapidStabAbilityActive", new RapidStabListener(), "迅刺生效");
            case 1 -> applyAbility("isBlindsideAbilityActive", new BlindsideListener(), "攻其不备生效");
            case 2 -> {
                if (applyAbility("isLeapStrikeAbilityActive", new LeapStrikeListener(), "跃斩生效")) {
                    applyAbility("isStunEffectActive", StunEffect.class, "晕眩生效");
                }
            }
            case 3 -> {
                if (applyAbility("isExposeWeaknessAbilityActive", new ExposeWeaknessListener(), "直取要害生效")) {
                    applyAbility("isStunEffectActive", StunEffect.class, "晕眩生效");
                }
            }
        }
    }

    private void applyDefenseAbility(int index) {
        AbilityEvolve.LOGGER.info("应用 defense 被动能力, 索引: " + index);
    }

    private void applyBuildingAbility(int index) {
        AbilityEvolve.LOGGER.info("应用 building 被动能力, 索引: " + index);
    }

    private void applyFarmingAbility(int index) {
        AbilityEvolve.LOGGER.info("应用 farming 被动能力, 索引: " + index);
    }

    private void applyAgilityAbility(int index) {
        AbilityEvolve.LOGGER.info("应用 agility 被动能力, 索引: " + index);
    }

    private void applyMagicAbility(int index) {
        AbilityEvolve.LOGGER.info("应用 magic 被动能力, 索引: " + index);
    }

    private void applyShootingAbility(int index) {
        AbilityEvolve.LOGGER.info("应用 shooting 被动能力, 索引: " + index);
    }

    private boolean applyAbility(String stateKey, Object listener, String message) {
        if (abilityStates.getOrDefault(stateKey, false)) return false;
        abilityStates.put(stateKey, true);
        System.out.println(message);
        MinecraftForge.EVENT_BUS.register(listener);
        return true;
    }
}
