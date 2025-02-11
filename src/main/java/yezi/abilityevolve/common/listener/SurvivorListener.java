package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.SurvivorAbility;

public class SurvivorListener {
    private final Player player;

    public SurvivorListener(Player player) {
        this.player = player;
    }

    @SubscribeEvent
    public void onPlayerDamaged(LivingDamageEvent event) {
        if (!(event.getEntity() == this.player)) return;
        Player player = (Player) event.getEntity();
        if (!player.equals(this.player)) return;
        SurvivorAbility survivorAbility = new SurvivorAbility();
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (survivorAbility.isUndead(attacker)) {
                float reducedDamage = survivorAbility.applyDamageReduction(player, event.getAmount());
                event.setAmount(reducedDamage);
            }
        }
    }
}

