package yezi.abilityevolve.common.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.Random;

public class AssociatedOreAbility extends Ability{
    private static final String name = "associated_ore";
    private static final String description = "Mining with a non-enchanted pickaxe has a chance to drop related ores.";
    private static final int requirement = 12;
    private static final Random random = new Random();

    public AssociatedOreAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.MINING.index, requirement
                        ),
                        new Requirement(
                                Skill.GATHERING.index, 10
                        )
                },
                "mining",
                0,
                6,
                true
        );
    }
    public final double dropChance = 0.25;
    public void NormalCoalDrop(BlockEvent.BreakEvent event){
            Block.popResource(event.getPlayer().level(), event.getPos(), new ItemStack(Items.COAL));
    }
    public void HighCoalDrop(BlockEvent.BreakEvent event){
            if (random.nextDouble() < 0.20) {
                ItemStack extraDrop = random.nextDouble() < 0.30 ? new ItemStack(Items.DIAMOND) : new ItemStack(Items.EMERALD);
                Block.popResource(event.getPlayer().level(), event.getPos(), extraDrop);
            } else {
                Block.popResource(event.getPlayer().level(), event.getPos(), new ItemStack(Items.COAL));
            }

    }
    public void extraDrop(BlockEvent.BreakEvent event, ItemStack extraOre){
            Block.popResource(event.getPlayer().level(), event.getPos(), extraOre);
    }
    public boolean isPickaxe(ItemStack itemStack){
        return itemStack.getItem() instanceof PickaxeItem
                && !itemStack.isEnchanted();
    }
    public boolean canDropExtraOre(Block block) {
      //  boolean b = block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE || block == Blocks.IRON_ORE || block == Blocks.NETHER_GOLD_ORE;
        return switch (GetAbilityLevel.getAbilityLevelMining3(ModCapabilities.getSkillModel(Minecraft.getInstance().player).getSkillLevel(Skill.MINING), requirement)) {
            case 1 -> block == Blocks.COAL_ORE;
            case 2 -> block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE;
            case 3 -> block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE || block == Blocks.IRON_ORE;
            case 4, 5 ->
                    block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE || block == Blocks.IRON_ORE || block == Blocks.NETHER_GOLD_ORE;
            default -> false;
        };
    }
}
