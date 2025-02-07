package yezi.skillablereforged.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.abilities.PassiveAbilityApplier;
import yezi.skillablereforged.common.capabilities.AbilityModel;
import yezi.skillablereforged.common.capabilities.AbilityProvider;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.capabilities.SkillProvider;
import yezi.skillablereforged.common.network.AbilitySyncToClient;
import yezi.skillablereforged.common.network.SyncToClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Mod.EventBusSubscriber(modid = "skillablereforged")
public class EventHandler {

    private static final int INTERVAL_TICKS = 100; // 5秒
    private static int tickCounter = 0;
    private static SkillModel lastDiedPlayerSkills = new SkillModel();
    private static AbilityModel lastDiedPlayerAbilities = new AbilityModel();
    private static final Map<UUID, PassiveAbilityApplier> abilityAppliers = new HashMap<>();

    public EventHandler() {

    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        SkillModel model = SkillModel.get(player);
        if (!player.isCreative() && (!model.canUseItem(player, item) || model.canUseBlock(player, block))) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        SkillModel model = SkillModel.get(player);
        if (!player.isCreative() && (!model.canUseItem(player, item) || model.canUseBlock(player, block))) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        ItemStack item = event.getItemStack();
        if (!player.isCreative() && (!SkillModel.get(player).canUseEntity(player, entity) || !SkillModel.get(player).canUseItem(player, item))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack item = player.getMainHandItem();

        if (!player.isCreative() && (!SkillModel.get(player).canUseItem(player, item) || !SkillModel.get(player).canAttackEntity(player, event.getTarget()))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onChangeEquipment(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (!player.isCreative() && event.getSlot().getType() == Type.ARMOR) {
                ItemStack item = event.getTo();
                if (!SkillModel.get(player).canUseItem(player, item)) {
                    player.drop(item.copy(),false);
                    item.setCount(0);
                }
            }
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onEntityDrops(LivingDropsEvent event) {
        if (Config.getDisableWool() && event.getEntity() instanceof Sheep) {
            event.getDrops().removeIf((item) -> item.getItem().is(ItemTags.WOOL));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (Config.getDeathReset()) {
                SkillModel.get(player).skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
            }
            lastDiedPlayerSkills = SkillModel.get(player);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            SkillModel skillModel = new SkillModel();
            SkillProvider provider = new SkillProvider(skillModel);
            AbilityModel abilityModel = new AbilityModel();
            AbilityProvider abilityProvider = new AbilityProvider(abilityModel);
            event.addCapability(new ResourceLocation("skillablereforged", "cap_skills"), provider);
            event.addCapability(new ResourceLocation("skillablereforged", "cap_abilities"), abilityProvider);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        SkillModel.get(event.getEntity()).skillLevels = lastDiedPlayerSkills.skillLevels;
        AbilityModel.get(event.getEntity()).grazieryLock = lastDiedPlayerAbilities.grazieryLock;
        AbilityModel.get(event.getEntity()).attackLock = lastDiedPlayerAbilities.attackLock;
        AbilityModel.get(event.getEntity()).defenseLock = lastDiedPlayerAbilities.defenseLock;
        AbilityModel.get(event.getEntity()).buildingLock = lastDiedPlayerAbilities.buildingLock;
        AbilityModel.get(event.getEntity()).farmingLock = lastDiedPlayerAbilities.farmingLock;
        AbilityModel.get(event.getEntity()).magicLock = lastDiedPlayerAbilities.magicLock;
        AbilityModel.get(event.getEntity()).agilityLock = lastDiedPlayerAbilities.agilityLock;
        AbilityModel.get(event.getEntity()).shootingLock = lastDiedPlayerAbilities.shootingLock;
        AbilityModel.get(event.getEntity()).gatheringLock = lastDiedPlayerAbilities.gatheringLock;
        AbilityModel.get(event.getEntity()).miningLock = lastDiedPlayerAbilities.miningLock;
        AbilityModel.get(event.getEntity()).abilityPoint = lastDiedPlayerAbilities.abilityPoint;
        AbilityModel.get(event.getEntity()).abilityPointCosts = lastDiedPlayerAbilities.abilityPointCosts;
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        SyncToClient.send(event.getEntity());
        AbilitySyncToClient.send(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        SyncToClient.send(event.getEntity());
        AbilitySyncToClient.send(event.getEntity());
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().isSpectator()) {
            SyncToClient.send(event.getEntity());
            AbilitySyncToClient.send(event.getEntity());
        }
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            if (tickCounter >= INTERVAL_TICKS) {
                tickCounter = 0;
                executePassiveAbilities();
            }
        }
    }
    private static void executePassiveAbilities() {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        Skillablereforged.LOGGER.info("executePassiveAbilities");
        if (server != null) {
            Skillablereforged.LOGGER.info("executePassiveAbilities" + server);
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UUID playerId = player.getUUID();
                abilityAppliers.computeIfAbsent(playerId, id -> new PassiveAbilityApplier(player));
                abilityAppliers.get(playerId).applyUnlockedAbilities();
            }
        }
    }
}