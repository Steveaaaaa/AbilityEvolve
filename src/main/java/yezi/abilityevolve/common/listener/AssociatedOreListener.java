package yezi.abilityevolve.common.listener;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.AssociatedOreAbility;

import java.util.Random;

public class AssociatedOreListener {
    private final AssociatedOreAbility associatedOreAbility = new AssociatedOreAbility();
    private static final Random random = new Random();
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Block brokenBlock = event.getState().getBlock();
        if (event.getPlayer() == null) return;
        if (associatedOreAbility.isPickaxe(event.getPlayer().getMainHandItem())) return;
        if (!associatedOreAbility.canDropExtraOre(brokenBlock)) return;
        if (random.nextDouble() < associatedOreAbility.dropChance){
            if (brokenBlock == Blocks.COAL_ORE && associatedOreAbility.abilityLevel == 5)
                associatedOreAbility.HighCoalDrop(event);
            else if (brokenBlock == Blocks.COAL_ORE)
                associatedOreAbility.NormalCoalDrop(event);
            else if (brokenBlock == Blocks.COPPER_ORE)
                associatedOreAbility.extraDrop(event, new ItemStack(Items.RAW_IRON));
            else if (brokenBlock == Blocks.IRON_ORE)
                associatedOreAbility.extraDrop(event, new ItemStack(Items.RAW_GOLD));
            else if (brokenBlock == Blocks.NETHER_GOLD_ORE)
                associatedOreAbility.extraDrop(event, new ItemStack(Items.RAW_GOLD));
        }
    }
}
