package yezi.skillablereforged.common.abilities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Requirement;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.utils.GetAbilityLevel;

import java.util.List;
import java.util.Random;

public class PanningAbility extends Ability {
    private static final String name = "panning";
    private static final String description = "Flint dropped from gravel has a chance to be replaced with iron nuggets, copper nuggets, or gold nuggets.";
    private static final int requirement = 12;
    GetAbilityLevel getAbilityLevel = new GetAbilityLevel();

    public int abilityLevel = getAbilityLevel.getAbilityLevelGathering2(SkillModel.get().getSkillLevel(Skill.GATHERING), requirement);
    public PanningAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.GATHERING, requirement
                        ),
                        new Requirement(
                                Skill.MINING, 8
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
    public ItemStack getRandomDrop() {
        List<ItemStack> possibleDrops = switch (abilityLevel) {
            case 1 -> List.of(new ItemStack(Items.RAW_COPPER));
            case 2 -> List.of(new ItemStack(Items.RAW_COPPER), new ItemStack(Items.IRON_NUGGET));
            case 3 -> List.of(new ItemStack(Items.RAW_COPPER), new ItemStack(Items.IRON_NUGGET), new ItemStack(Items.GOLD_NUGGET));
            default -> List.of();
        };
        return possibleDrops.isEmpty() ? ItemStack.EMPTY : possibleDrops.get(RANDOM.nextInt(possibleDrops.size()));
    }
}
