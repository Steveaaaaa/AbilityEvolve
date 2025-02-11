package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.abilities.LeapStrikeAbility;
public class LeapStrikeListener {
    private final ServerPlayer player;
    private final LeapStrikeAbility ability;
    public LeapStrikeListener(ServerPlayer player) {
        this.player = player;
        this.ability = new LeapStrikeAbility();
    }
    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() == this.player)) return;
        if (player.isFallFlying() || player.getAbilities().flying || ability.leapingPlayers.contains(player.getUUID())) return;
        if (player.onGround()) {return;}
        AbilityEvolve.LOGGER.info("玩家 {} 触发跃斩！", player.getName().getString() + "目标为:" + event.getEntity().getName().getString());
        LivingEntity target = event.getEntity();
        ability.triggerLeapStrike(player, target);
    }
}
