package yezi.abilityevolve.common.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.Set;

public class GreedyAbility extends Ability{
    private static final String name = "greedy";
    private static final String description = "When holding a tool in the main hand, mining range +3.";
    private static final int requirement = 16;

    public GreedyAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.MINING.index, requirement
                        ),
                        new Requirement(
                                Skill.BUILDING.index, 12
                        )
                },
                "mining",
                1,
                8,
                true
        );
    }
    private static final Set<Class<? extends Item>>[] ALLOWED_TOOLS = new Set[]{
            Set.of(AxeItem.class),
            Set.of(AxeItem.class, ShearsItem.class),
            Set.of(AxeItem.class, ShearsItem.class, HoeItem.class),
            Set.of(AxeItem.class, ShearsItem.class, HoeItem.class, ShovelItem.class),
            Set.of(AxeItem.class, ShearsItem.class, HoeItem.class, ShovelItem.class, PickaxeItem.class)
    };

    public boolean isToolValid(ItemStack tool, ServerPlayer player) {
        int abilityLevel = GetAbilityLevel.getAbilityLevelMining2(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.MINING), requirement);
        Item item = tool.getItem();
        return ALLOWED_TOOLS[abilityLevel - 1].stream().anyMatch(type -> type.isInstance(item));
    }
    public void applyEffect(ServerPlayer player, BlockPos pos) {
        if (isToolValid(player.getMainHandItem(),player)) {
            int range = 3;
            for (BlockPos p : BlockPos.betweenClosed(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
                BlockState state = player.level().getBlockState(p);
                ItemStack tool = player.getMainHandItem();
                if (state.isAir() || !tool.isCorrectToolForDrops(state)) {
                    continue;
                }
                player.level().destroyBlock(p, true, player);
            }
        }
    }
}

