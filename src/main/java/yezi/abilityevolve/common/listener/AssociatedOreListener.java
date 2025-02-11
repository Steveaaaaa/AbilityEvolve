package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.AssociatedOreAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.Random;

public class AssociatedOreListener {
    private final Player player;
    private final AssociatedOreAbility associatedOreAbility;
    private static final Random random = new Random();

    public AssociatedOreListener(Player player) {
        this.player = player;
        this.associatedOreAbility = new AssociatedOreAbility();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (this.player != player) return;

        Block brokenBlock = event.getState().getBlock();
        if (!associatedOreAbility.isPickaxe(player.getMainHandItem())) return;

        int abilityLevel = GetAbilityLevel.getAbilityLevelMining3(
                ModCapabilities.getSkillModel(this.player).getSkillLevel(Skill.MINING),
                AssociatedOreAbility.requirement
        );

        if (!associatedOreAbility.canDropExtraOre(brokenBlock, player)) return;

        if (random.nextDouble() < associatedOreAbility.dropChance) {
            if (brokenBlock == Blocks.COAL_ORE && abilityLevel == 5)
                associatedOreAbility.HighCoalDrop(event, player);
            else if (brokenBlock == Blocks.COAL_ORE)
                associatedOreAbility.NormalCoalDrop(event, player);
            else if (brokenBlock == Blocks.COPPER_ORE)
                associatedOreAbility.extraDrop(event, new ItemStack(Items.RAW_IRON), player);
            else if (brokenBlock == Blocks.IRON_ORE)
                associatedOreAbility.extraDrop(event, new ItemStack(Items.RAW_GOLD), player);
            else if (brokenBlock == Blocks.NETHER_GOLD_ORE)
                associatedOreAbility.extraDrop(event, new ItemStack(Items.RAW_GOLD), player);
        }
    }
}


