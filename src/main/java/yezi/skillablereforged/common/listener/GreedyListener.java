package yezi.skillablereforged.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.common.abilities.GreedyAbility;

public class GreedyListener {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            GreedyAbility greedyAbility = new GreedyAbility();
            greedyAbility.applyEffect(player, event.getPos());
        }
    }
}
