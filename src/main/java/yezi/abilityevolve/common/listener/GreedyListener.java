package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.GreedyAbility;

public class GreedyListener {
    private final ServerPlayer player;
    public GreedyListener(ServerPlayer player) {
        this.player = player;
    }
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == this.player) {
            GreedyAbility greedyAbility = new GreedyAbility();
            greedyAbility.applyEffect(player, event.getPos());
        }
    }
}


