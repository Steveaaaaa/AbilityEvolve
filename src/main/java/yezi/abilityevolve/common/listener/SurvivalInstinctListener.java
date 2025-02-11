package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.SurvivalInstinctAbility;

public class SurvivalInstinctListener {
    private final Player player;
    private final SurvivalInstinctAbility ability;

    public SurvivalInstinctListener(Player player) {
        this.player = player;
        this.ability = new SurvivalInstinctAbility();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player == this.player && !event.player.level().isClientSide) {
            ability.checkAndRemoveNegativeEffects(player);
        }
    }
}

