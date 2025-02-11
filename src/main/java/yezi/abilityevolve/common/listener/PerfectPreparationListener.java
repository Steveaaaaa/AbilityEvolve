package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.PerfectPreparationAbility;

public class PerfectPreparationListener {
    private final Player player;
    public PerfectPreparationListener(Player player) {
        this.player = player;
    }

    @SubscribeEvent
    public void onPlayerDamage(LivingDamageEvent event) {
        if (!(event.getEntity() == this.player)) return;
        if (player.getHealth() - event.getAmount() <= 0) {
            PerfectPreparationAbility.triggerAbility(player);
            event.setCanceled(true);
        }
        if (PerfectPreparationAbility.isInvincible(player)) {
            event.setCanceled(true);
        }
    }
}
