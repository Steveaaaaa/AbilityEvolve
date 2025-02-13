package yezi.abilityevolve.common.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.TransformAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

public class TransformListener {
    private final Player player;
    public TransformListener(Player player) {
        this.player = player;
    }
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() != this.player) return;
        Player player = event.getEntity();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean holdingChorus = mainHand.getItem() == Items.CHORUS_FRUIT ||
                offHand.getItem() == Items.CHORUS_FRUIT;

        if (holdingChorus && !event.isCanceled()) {
            int level = getPlayerLevel(this.player);
            if (level > 0) {
                boolean success = TransformAbility.transmuteBlock(player, level, state, world, pos);
                if (success) {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
    }

    private int getPlayerLevel(Player player) {
        return GetAbilityLevel.getAbilityLevelAttack1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.BUILDING), TransformAbility.requirement);
    }
}
