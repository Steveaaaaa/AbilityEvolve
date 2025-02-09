package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.RapidStabAbility;

public class RapidStabListener {
    private final RapidStabAbility rapidStabAbility = new RapidStabAbility();
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        rapidStabAbility.applyEffect(player);
     //   new RapidStabAbility().applyEffect(player);
    }
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        rapidStabAbility.applyEffect(player);
    }
    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        rapidStabAbility.applyEffect(player);
    }
}
