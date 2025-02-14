package yezi.abilityevolve.common.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "abilityevolve")
public class PlayerSoundDetector {
    @SubscribeEvent
    public void onPlayerSound(PlayerInteractEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() != null) {
            triggerSoundEvent(event.getEntity());
        }
    }
    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player && event.getDistance() > 2) {
            triggerSoundEvent(player);
        }
    }
    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        triggerSoundEvent(event.getPlayer());
    }
    @SubscribeEvent
    public static void onArmorChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player)
            triggerSoundEvent((Player) event.getEntity());
    }
    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player) {
            triggerSoundEvent(player);
        }
    }
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        triggerSoundEvent(event.getPlayer());
    }
    private static void triggerSoundEvent(Player player) {
        if (!player.level().isClientSide) {
            MinecraftForge.EVENT_BUS.post(new PlayerSoundEvent(player));
        }
    }
}
