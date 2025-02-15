package yezi.abilityevolve.common.abilities;

import net.minecraft.world.entity.player.Player;
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
//这是一个挖掘技能，可以根据玩家挖掘等级和技能等级来决定概率和额外掉落物。
public class AssociatedOreAbility extends Ability {
    private static final String name = "associated_ore";
    private static final String description = "Mining with a non-enchanted pickaxe has a chance to drop related ores.";
    public static final int requirement = 12;
    private static final Random random = new Random();

    public AssociatedOreAbility() {
        super(
                name,
                description,
                new Requirement[] {
                        new Requirement(Skill.MINING.index, requirement),
                        new Requirement(Skill.GATHERING.index, 10)
                },
                "mining",
                0,
                6,
                true
        );
    }

    public double dropChance = 0.25;  // 也改为实例字段

    public void NormalCoalDrop(BlockEvent.BreakEvent event, Player player) {
        Block.popResource(player.level(), event.getPos(), new ItemStack(Items.COAL));
    }

    public void HighCoalDrop(BlockEvent.BreakEvent event, Player player) {
        if (random.nextDouble() < 0.20) {
            ItemStack extraDrop = random.nextDouble() < 0.30 ? new ItemStack(Items.DIAMOND) : new ItemStack(Items.EMERALD);
            Block.popResource(player.level(), event.getPos(), extraDrop);
        } else {
            Block.popResource(player.level(), event.getPos(), new ItemStack(Items.COAL));
        }
    }

    public void extraDrop(BlockEvent.BreakEvent event, ItemStack extraOre, Player player) {
        Block.popResource(player.level(), event.getPos(), extraOre);
    }

    public boolean isPickaxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof PickaxeItem && !itemStack.isEnchanted();
    }

    public boolean canDropExtraOre(Block block, Player player) {
        int abilityLevel = GetAbilityLevel.getAbilityLevelMining3(
                ModCapabilities.getSkillModel(player).getSkillLevel(Skill.MINING),
                requirement
        );
        return switch (abilityLevel) {
            case 1 -> block == Blocks.COAL_ORE;
            case 2 -> block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE;
            case 3 -> block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE || block == Blocks.IRON_ORE;
            case 4, 5 -> block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE || block == Blocks.IRON_ORE || block == Blocks.NETHER_GOLD_ORE;
            default -> false;
        };
    }
}

