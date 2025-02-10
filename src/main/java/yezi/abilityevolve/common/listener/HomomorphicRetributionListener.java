package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.HomomorphicRetributionAbility;

public class HomomorphicRetributionListener {
    private static final HomomorphicRetributionAbility ability = new HomomorphicRetributionAbility();
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.PlayerTickEvent.Phase.END) {
            ability.applyEffect(player);
        }
    }
}
