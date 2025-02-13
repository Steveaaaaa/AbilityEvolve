package yezi.abilityevolve.common.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformAbility extends Ability{
    private static final String name = "transform";
    private static final String description = "The Midas touch.";
    public static final int requirement = 22;
    public TransformAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.BUILDING.index, requirement
                        ),
                        new Requirement(
                                Skill.MAGIC.index, 16
                        )
                },
                "building",
                2,
                10,
                false
        );
    }
    public record TransmutationRecipe(Block input, Block output) {}
    private static final Map<Integer, List<TransmutationRecipe>> TRANSFORM_RECIPES = new HashMap<>();
    private static final Map<Integer, TransmutationRecipe> GOLDEN_RECIPES = new HashMap<>();
    static {
        addTransformRecipe(1, Blocks.COAL_ORE, Blocks.MAGMA_BLOCK);
        addTransformRecipe(1, Blocks.CLAY, Blocks.SOUL_SOIL);
        addTransformRecipe(2, Blocks.QUARTZ_BLOCK, Blocks.AMETHYST_BLOCK);
        addTransformRecipe(2, Blocks.SAND, Blocks.SOUL_SAND);
        addTransformRecipe(3, Blocks.GOLD_ORE, Blocks.GILDED_BLACKSTONE);
        addTransformRecipe(3, Blocks.MOSS_BLOCK, Blocks.SCULK);
        addTransformRecipe(4, Blocks.REDSTONE_ORE, Blocks.GLOWSTONE);
        addTransformRecipe(4, Blocks.GRASS_BLOCK, Blocks.MYCELIUM);
        addTransformRecipe(5, Blocks.PURPUR_BLOCK, Blocks.PRISMARINE);
        addTransformRecipe(5, Blocks.EMERALD_ORE, Blocks.DIAMOND_ORE);

        addGoldenRecipe(6, Blocks.IRON_ORE, Blocks.GOLD_ORE);
        addGoldenRecipe(7, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL);
        addGoldenRecipe(8, Blocks.COPPER_ORE, Blocks.LAPIS_ORE);
        addGoldenRecipe(9, Blocks.CRYING_OBSIDIAN, Blocks.DEEPSLATE_DIAMOND_ORE);
        addGoldenRecipe(10, Blocks.REINFORCED_DEEPSLATE, Blocks.ANCIENT_DEBRIS);
    }
    public static void addTransformRecipe(int level, Block input, Block output) {
        TRANSFORM_RECIPES.computeIfAbsent(level, k -> new ArrayList<>(2)).add(new TransmutationRecipe(input, output));
    }
    public static void addGoldenRecipe(int level, Block input, Block output) {
        GOLDEN_RECIPES.put(level, new TransmutationRecipe(input, output));
    }
    public static boolean transmuteBlock(Player player, int level, BlockState targetBlock, Level world, BlockPos pos) {
        level = Math.min(level, 10);
        return level <= 5 ? handleTransform(player, level, targetBlock, world, pos) : handleGolden(player, level, targetBlock, world, pos);
    }
    private static boolean handleTransform(Player player, int level, BlockState targetBlock, Level world, BlockPos pos) {
        for (int lv = Math.min(level, 5); lv >= 1; lv--) {
            List<TransmutationRecipe> recipes = TRANSFORM_RECIPES.get(lv);
            if (recipes != null) {
                for (TransmutationRecipe recipe : recipes) {
                    if (targetBlock.is(recipe.input())) {
                        replaceBlock(world, pos, recipe.output());
                        consumeItem(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean handleGolden(Player player, int level, BlockState targetBlock, Level world, BlockPos pos) {
        final int cost = Math.max(level - 5, 1);

        for (int lv = Math.min(level, 10); lv >= 6; lv--) {
            TransmutationRecipe recipe = GOLDEN_RECIPES.get(lv);
            if (recipe != null && targetBlock.is(recipe.input())) {
                if (player.experienceLevel >= cost) {
                    replaceBlock(world, pos, recipe.output());
                    consumeItem(player);
                    consumeExperience(player, cost);
                    return true;
                } else {
                    player.displayClientMessage(
                            Component.translatable("message.transmutation.need_xp")
                                    .append(String.valueOf(cost)),
                            true
                    );
                    return false;
                }
            }
        }
        return handleTransform(player, 5, targetBlock, world, pos);
    }
    private static void replaceBlock(Level world, BlockPos pos, Block newBlock) {
        if (!world.isClientSide) {
            world.setBlock(pos, newBlock.defaultBlockState(), 3);
            world.levelEvent(2001, pos, Block.getId(newBlock.defaultBlockState()));
        }
    }
    private static void consumeItem(Player player) {
        ItemStack item = player.getMainHandItem();
        if (item.getItem() == Items.CHORUS_FRUIT) {
            item.shrink(1);
        } else {
            item = player.getOffhandItem();
            if (item.getItem() == Items.CHORUS_FRUIT) {
                item.shrink(1);
            }
        }
    }
    private static void consumeExperience(Player player, int levels) {
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-levels);

            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.ENCHANT,
                        player.getX(),
                        player.getY() + 1.0,
                        player.getZ(),
                        20,
                        0.5, 0.5, 0.5,
                        0.2
                );
            }
        }
    }
}
