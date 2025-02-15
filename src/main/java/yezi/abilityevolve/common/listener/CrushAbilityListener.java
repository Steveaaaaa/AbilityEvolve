package yezi.abilityevolve.common.listener;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yezi.abilityevolve.common.capabilities.CrushAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

@Mod.EventBusSubscriber(modid = "abilityevolve", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrushAbilityListener {
    public static final int[] chargeThreshold = {6,6,6,5,5,5,5,4,4,4};
    public static final float[] damagePercent = {170.0f,180.0f,190.0f,200.0f,215.0f,225.0f,235.0f,245.0f,255.0f,270.0f};
    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();

        if (event.getHand() == InteractionHand.MAIN_HAND
                && target instanceof IronGolem golem
                && (stack.getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof AnvilBlock)) {

            int L = GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.MAGIC), CrushAbility.requirement);

            golem.getCapability(ModCapabilities.CRUSH_CAPABILITY).ifPresent(crush -> {
                crush.setParameters(chargeThreshold[L-1], damagePercent[L-1]);

                if (!player.isCreative()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    }
                }

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
            });
        }
    }

    @SubscribeEvent
    public static void onGolemHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof IronGolem) {
            entity.getCapability(ModCapabilities.CRUSH_CAPABILITY).ifPresent(crush -> {
                crush.onHurt(entity);
            });
        }
    }
}
