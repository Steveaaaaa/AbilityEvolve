package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.ExplosiveMiningAbility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExplosiveMiningListener {
    private final ServerPlayer player;
    private final ExplosiveMiningAbility explosiveMiningAbility;
    public ExplosiveMiningListener(ServerPlayer player) {
        this.player = player;
        this.explosiveMiningAbility = new ExplosiveMiningAbility();
    }
    private static final Map<UUID, Float> damageReductionMap = new HashMap<>();
    @SubscribeEvent
    public void onTNTPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() != this.player) return;
        explosiveMiningAbility.onTNTPlaced(player, event.getPos());
        damageReductionMap.put(player.getUUID(), explosiveMiningAbility.DAMAGE_REDUCTION[explosiveMiningAbility.getAbilityLevel(player) - 1]);
    }
    @SubscribeEvent
    public void onExplosionDamage(LivingDamageEvent event) {
        if (event.getEntity() == player) {
            if (event.getSource().is(DamageTypes.EXPLOSION)) {
                damageReductionMap.put(player.getUUID(), explosiveMiningAbility.DAMAGE_REDUCTION[explosiveMiningAbility.getAbilityLevel(player) - 1]);
                if (damageReductionMap.containsKey(player.getUUID())) {
                    float reduction = damageReductionMap.get(player.getUUID());
                    event.setAmount(event.getAmount() * (1 - reduction));
                }
            }
        }
    }
}

