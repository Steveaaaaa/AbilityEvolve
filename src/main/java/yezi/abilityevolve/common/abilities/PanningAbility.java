package yezi.abilityevolve.common.abilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.List;
import java.util.Random;

public class PanningAbility extends Ability {
    private static final String name = "panning";
    private static final String description = "Flint dropped from gravel has a chance to be replaced with iron nuggets, copper nuggets, or gold nuggets.";
    private static final int requirement = 12;

    public PanningAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.GATHERING.index, requirement
                        ),
                        new Requirement(
                                Skill.MINING.index, 8
                        )
                },
                "gathering",
                0,
                4,
                true
        );
    }

    public boolean shouldReplaceDrop() {
        float chance = 0.2f;
        return RANDOM.nextFloat() < chance;
    }
    private static final Random RANDOM = new Random();
    public ItemStack getRandomDrop(Player player) {
        List<ItemStack> possibleDrops = switch ( GetAbilityLevel.getAbilityLevelGathering2(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.GATHERING), requirement)) {
            case 1 -> List.of(new ItemStack(Items.RAW_COPPER));
            case 2 -> List.of(new ItemStack(Items.RAW_COPPER), new ItemStack(Items.IRON_NUGGET));
            case 3 -> List.of(new ItemStack(Items.RAW_COPPER), new ItemStack(Items.IRON_NUGGET), new ItemStack(Items.GOLD_NUGGET));
            default -> List.of();
        };
        return possibleDrops.isEmpty() ? ItemStack.EMPTY : possibleDrops.get(RANDOM.nextInt(possibleDrops.size()));
    }
}
