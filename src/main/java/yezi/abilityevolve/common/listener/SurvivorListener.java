package yezi.abilityevolve.common.listener;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.abilities.SurvivorAbility;
import yezi.abilityevolve.common.network.SurvivorParticleEffectPacket;

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
                MinecraftServer server = player.getServer();
                float reducedDamage = survivorAbility.applyDamageReduction(player, event.getAmount());
                event.setAmount(reducedDamage);

                Vec3 playerPos = player.position();
                Vec3 attackerPos = attacker.position();

                if (server != null) {
                    AbilityEvolve.LOGGER.info("Survivor Rendering.");
                    SurvivorParticleEffectPacket.sendToAllClients(playerPos, attackerPos, "Protecting Undead Attack");
                }
            }
        }
    }
}

