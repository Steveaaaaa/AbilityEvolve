package yezi.skillablereforged.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;
public class LeapStrikeListener {
    private static final LeapStrikeAbility ability = new LeapStrikeAbility();
    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
     //   Skillablereforged.LOGGER.info("onAttack");
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (player.isFallFlying() || player.getAbilities().flying) return;
        if (player.onGround()){
            ability.onPlayerLand(player);
            return;
        }
        Skillablereforged.LOGGER.info("玩家 {} 跃斩生效！", player.getName().getString());
        LivingEntity target = event.getEntity();
        ability.triggerLeapStrike(player, target);
    }
}
