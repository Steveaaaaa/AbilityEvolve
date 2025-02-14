package yezi.abilityevolve.common.abilities;

import net.minecraft.server.level.ServerPlayer;
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
    private final Player player;

    public PassiveAbilityApplier(Player player) {
        this.player = player;
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
        List<Pair<String, Integer>> unlockedAbilities = abilityManager.getUnlockedPassiveAbilities();
        for (Pair<String, Integer> ability : unlockedAbilities) {
            abilityHandlers.getOrDefault(ability.getLeft(), index ->
                    AbilityEvolve.LOGGER.info("Unknown Ability: " + ability.getLeft())
            ).accept(ability.getRight());
        }
    }

    private void applyGrazieryAbility(int index) {
        switch (index) {
            case 0 -> applyAbility("isAidSupportAbilityActive", new AidSupportListener(player), "援护生效");
            case 1 -> applyAbility("isRidingAbilityActive", new ConcentratedFeedingAbilityListener(player), "精饲生效");
            case 2 -> applyAbility("isIronCavalryAbilityActive", new IronCavalryListener((ServerPlayer) player), "铁骑生效");
            case 3 -> applyAbility("isWolvesAbilityActive", new WolvesAbilityListener(player), "狼群生效");
        }
    }

    private void applyMiningAbility(int index) {
        switch (index) {
            case 0 -> applyAbility("isAssociatedOreAbilityActive", new AssociatedOreListener(player), "伴生生效");
            case 1 -> applyAbility("isGreedyAbilityActive", new GreedyListener((ServerPlayer) player), "贪婪生效");
            case 2 -> applyAbility("isExplosiveAbilityActive", new ExplosiveMiningListener((ServerPlayer) player), "爆破掘进生效");
        }
    }

    private void applyGatheringAbility(int index) {
        if (index == 0) applyAbility("isPanningAbilityActive", new PanningListener((ServerPlayer) player), "淘金生效");
    }

    private void applyAttackAbility(int index) {
        switch (index) {
            case 0 -> applyAbility("isRapidStabAbilityActive", new RapidStabListener((ServerPlayer) player), "迅刺生效");
            case 1 -> applyAbility("isBlindsideAbilityActive", new BlindsideListener((ServerPlayer) player), "攻其不备生效");
            case 2 -> {
                if (applyAbility("isLeapStrikeAbilityActive", new LeapStrikeListener((ServerPlayer) player), "跃斩生效")) {
                    applyAbility("isStunEffectActive", StunEffect.class, "晕眩生效");
                }
            }
            case 3 -> {
                if (applyAbility("isExposeWeaknessAbilityActive", new ExposeWeaknessListener(player), "直取要害生效")) {
                    applyAbility("isStunEffectActive", StunEffect.class, "晕眩生效");
                }
            }
        }
    }

    private void applyDefenseAbility(int index) {

   //     AbilityEvolve.LOGGER.info("应用 defense 被动能力, 索引: " + index);
        switch (index) {
            case 0 -> applyAbility("isHomomorphicRetributionAbilityActive", new HomomorphicRetributionListener(player), "同态复仇生效");
            case 1 -> applyAbility("isPerfectPreparationAbilityActive", new PerfectPreparationListener(player), "准备万全生效");
            case 2 -> applyAbility("isSurvivorAbilityActive", new SurvivorListener(player), "幸存者生效");
            case 3 -> applyAbility("isSurvivalInstinctAbilityActive", new SurvivalInstinctListener(player), "生还本能生效");
        }
    }

    private void applyBuildingAbility(int index) {

//        AbilityEvolve.LOGGER.info("应用 building 被动能力, 索引: " + index);
        switch (index) {
            case 2 -> applyAbility("isTransformAbilityActive", new TransformListener(player), "紫颂果嬗变术生效");
        }
    }

    private void applyFarmingAbility(int index) {
      //  AbilityEvolve.LOGGER.info("应用 farming 被动能力, 索引: " + index);
        switch (index) {
            case 1 -> applyAbility("isHarvestAbilityActive", new HarvestListener(player), "收割生效");
            case 2 -> {
                if (!PlayerTickListener.energeticUnlockedMap.containsKey(player.getUUID()))
                    PlayerTickListener.energeticUnlockedMap.put(this.player.getUUID(), false);
            }
        }
    }

    private void applyAgilityAbility(int index) {

      //  AbilityEvolve.LOGGER.info("应用 agility 被动能力, 索引: " + index);
        switch (index) {
            case 1 -> PlayerTickListener.longTravelUnlockedMap.put(this.player.getUUID(), false);
            case 2 -> PlayerTickListener.concealUnlockedMap.put(this.player.getUUID(), true);
        }
    }

    private void applyMagicAbility(int index) {
       // AbilityEvolve.LOGGER.info("应用 magic 被动能力, 索引: " + index);
    }

    private void applyShootingAbility(int index) {

        //AbilityEvolve.LOGGER.info("应用 shooting 被动能力, 索引: " + index);
    }

    private boolean applyAbility(String stateKey, Object listener, String message) {
        if (abilityStates.getOrDefault(stateKey, false)) return false;
        abilityStates.put(stateKey, true);
        System.out.println(message);
        MinecraftForge.EVENT_BUS.register(listener);
        return true;
    }
}
