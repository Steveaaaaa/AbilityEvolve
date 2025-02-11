package yezi.abilityevolve.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.Event;

public class PlayerInHotBlockEvent extends Event{
    private final Player player;
    private final BlockPos position;
    private final Block block;
    private final boolean inFire;
    private final boolean inSoulFire;
    private final boolean inLava;

    public PlayerInHotBlockEvent(Player player, BlockPos position, Block block, boolean inFire, boolean inSoulFire, boolean inLava) {
        this.player = player;
        this.position = position;
        this.block = block;
        this.inFire = inFire;
        this.inSoulFire = inSoulFire;
        this.inLava = inLava;
    }
    public Player getPlayer() {
        return player;
    }
    public BlockPos getPosition() {
        return position;
    }

    public Block getBlock() {
        return block;
    }
    public boolean isInSoulFire() {
        return inSoulFire;
    }

    public boolean isInLava() {
        return inLava;
    }

    public boolean isInFire() {
        return inFire;
    }
}
