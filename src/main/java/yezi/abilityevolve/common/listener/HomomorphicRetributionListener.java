package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.HomomorphicRetributionAbility;
import yezi.abilityevolve.common.events.PlayerInHotBlockEvent;

public class HomomorphicRetributionListener {
    private final Player player;
    private final HomomorphicRetributionAbility ability;
    public HomomorphicRetributionListener(Player player) {
        this.player = player;
        this.ability = new HomomorphicRetributionAbility();
    }
    @SubscribeEvent
    public void onPlayerEnterHotBlock(PlayerInHotBlockEvent event) {
        ability.startAbility(player, event.isInSoulFire(), event.isInLava());
    }
}

