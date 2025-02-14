package yezi.abilityevolve.common.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class PlayerSoundEvent extends Event {
    private final Player player;

    public PlayerSoundEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}