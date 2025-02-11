package yezi.abilityevolve.common.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.PanningAbility;

import java.util.List;

public class PanningListener {
    private final ServerPlayer player;
    private final PanningAbility panningAbility;
    public PanningListener(ServerPlayer player) {
        this.player = player;
        this.panningAbility = new PanningAbility();
    }
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() == this.player)) return;
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        if (state.getBlock() == Blocks.GRAVEL) {
            if (panningAbility.shouldReplaceDrop()) {
                ItemStack replacementDrop = panningAbility.getRandomDrop();
                List<ItemStack> originalDrops = Block.getDrops(state, (ServerLevel) level, pos, level.getBlockEntity(pos));
                for (ItemStack drop : originalDrops) {
                    drop.setCount(0);
                }
                Block.popResource(level, pos, replacementDrop);
            }
        }
    }
}

