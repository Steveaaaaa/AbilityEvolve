package yezi.abilityevolve.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "abilityevolve")
public class PlayerHotBlockDetector {
    private static final Map<UUID, Boolean> lastStateMap = new HashMap<>();
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if (player == null || player.isSpectator()) return;

        UUID playerUUID = player.getUUID();
        Level world = player.level();
        BlockPos pos = player.blockPosition();

        boolean isInHotBlock = isInHotBlock(pos, world);
        boolean isInFire = isInFire(pos, world);
        boolean isInSoulFire = isInSoulFire(pos, world);
        boolean isInLava = isInLava(pos, world);
        boolean lastState = lastStateMap.getOrDefault(playerUUID, false);

        if (isInHotBlock != lastState) {
            MinecraftForge.EVENT_BUS.post(new PlayerInHotBlockEvent(player, pos, world.getBlockState(pos).getBlock(), isInFire, isInSoulFire, isInLava));
            lastStateMap.put(playerUUID, isInHotBlock);
        }
    }
    private static boolean isInHotBlock(BlockPos pos, Level world) {
        return world.getBlockState(pos).is(Blocks.FIRE) || world.getBlockState(pos).is(Blocks.SOUL_FIRE) || world.getBlockState(pos).is(Blocks.LAVA);
    }

    private static boolean isInSoulFire(BlockPos pos, Level world) {
        return world.getBlockState(pos).is(Blocks.SOUL_FIRE);
    }
    private static boolean isInFire(BlockPos pos, Level world) {
        return world.getBlockState(pos).is(Blocks.FIRE);
    }
    private static boolean isInLava(BlockPos pos, Level world) {
        return world.getBlockState(pos).is(Blocks.LAVA);
    }
}
