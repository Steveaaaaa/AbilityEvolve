package yezi.abilityevolve.common.listener;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.abilities.PassiveAbilityApplier;
import yezi.abilityevolve.common.capabilities.AbilityModel;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.network.AbilitySyncToClient;
import yezi.abilityevolve.common.network.SyncToClient;
import yezi.abilityevolve.common.skills.SkillRequirementChecker;
import yezi.abilityevolve.config.ConfigManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class EventHandler {

    private static final int INTERVAL_TICKS = 100;
    private static int tickCounter = 0;
    private static SkillModel lastDiedPlayerSkills = new SkillModel();
    private static AbilityModel lastDiedPlayerAbilities = new AbilityModel();
    private static final Map<UUID, PassiveAbilityApplier> abilityAppliers = new HashMap<>();

    private EventHandler() {}

    // ==============================
    // 1. 玩家交互事件处理
    // ==============================
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        handleInteraction(event.getEntity(), event.getItemStack(), event.getLevel().getBlockState(event.getPos()).getBlock(), event);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        handleInteraction(event.getEntity(), event.getItemStack(), event.getLevel().getBlockState(event.getPos()).getBlock(), event);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (!player.isCreative() && !SkillRequirementChecker.canUseItem(player, event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if (!player.isCreative() && (!SkillRequirementChecker.canUseEntity(player, entity) || !SkillRequirementChecker.canUseItem(player, event.getItemStack()))) {
            event.setCanceled(true);
        }
    }

    private static void handleInteraction(Player player, ItemStack item, Block block, net.minecraftforge.eventbus.api.Event event) {
        if (!player.isCreative() && (!SkillRequirementChecker.canUseItem(player, item) || SkillRequirementChecker.canUseBlock(player, block))) {
            if (event instanceof PlayerInteractEvent) {
                event.setCanceled(true);
            }
        }
    }

    // ==============================
    // 2. 玩家攻击与装备变更事件
    // ==============================
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!player.isCreative() && (!SkillRequirementChecker.canUseItem(player, player.getMainHandItem()) || !SkillRequirementChecker.canAttackEntity(player, event.getTarget()))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onChangeEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && !player.isCreative() && event.getSlot().getType() == EquipmentSlot.Type.ARMOR) {
            ItemStack item = event.getTo();
            if (!SkillRequirementChecker.canUseItem(player, item)) {
                player.drop(item.copy(), false);
                item.setCount(0);
            }
        }
    }

    // ==============================
    // 3. 实体掉落 & 玩家死亡、克隆
    // ==============================
    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (ConfigManager.getDisableWool() && event.getEntity() instanceof Sheep) {
            event.getDrops().removeIf(drop -> drop.getItem().is(ItemTags.WOOL));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            ModCapabilities.getOptionalSkillModel(player).ifPresent(model -> {
                if (ConfigManager.getDeathReset()) {
                    Arrays.fill(model.skillLevels, 1);
                }
                lastDiedPlayerSkills = model;
            });

            ModCapabilities.getOptionalAbilityModel(player).ifPresent(model -> lastDiedPlayerAbilities = model);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        ModCapabilities.getOptionalSkillModel(player).ifPresent(model -> model.skillLevels = Arrays.copyOf(lastDiedPlayerSkills.skillLevels, lastDiedPlayerSkills.skillLevels.length));
        ModCapabilities.getOptionalAbilityModel(player).ifPresent(model -> {
            model.grazieryLock = lastDiedPlayerAbilities.grazieryLock;
            model.attackLock = lastDiedPlayerAbilities.attackLock;
            model.defenseLock = lastDiedPlayerAbilities.defenseLock;
            model.buildingLock = lastDiedPlayerAbilities.buildingLock;
            model.farmingLock = lastDiedPlayerAbilities.farmingLock;
            model.magicLock = lastDiedPlayerAbilities.magicLock;
            model.agilityLock = lastDiedPlayerAbilities.agilityLock;
            model.shootingLock = lastDiedPlayerAbilities.shootingLock;
            model.gatheringLock = lastDiedPlayerAbilities.gatheringLock;
            model.miningLock = lastDiedPlayerAbilities.miningLock;
            model.abilityPoint = lastDiedPlayerAbilities.abilityPoint;
            model.abilityPointCosts = lastDiedPlayerAbilities.abilityPointCosts;
        });
    }

    // ==============================
    // 4. 玩家数据同步事件
    // ==============================
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncPlayerData(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncPlayerData(event.getEntity());
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().isSpectator()) {
            syncPlayerData(event.getEntity());
        }
    }

    private static void syncPlayerData(Player player) {
        SyncToClient.send(player);
        AbilitySyncToClient.send(player);
    }

    // ==============================
    // 5. 被动能力执行（定期触发）
    // ==============================
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && ++tickCounter >= INTERVAL_TICKS) {
            AbilityEvolve.LOGGER.info("onServerTick");
            tickCounter = 0;
            executePassiveAbilities();
        }
    }

    private static void executePassiveAbilities() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                AbilityEvolve.LOGGER.info("执行被动能力: " + player.getUUID());
                abilityAppliers.computeIfAbsent(player.getUUID(), id -> new PassiveAbilityApplier(player))
                        .applyUnlockedAbilities();
            }
        }
    }
}
