package yezi.skillablereforged.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.common.abilities.BlindsideAbility;

public class BlindsideListener {
    private final BlindsideAbility blindsideAbility = new BlindsideAbility();
    @SubscribeEvent
    public void onPlayerAttack(LivingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer)) {
            return;
        }
        LivingEntity target = event.getEntity();
            float multiplier = blindsideAbility.getBonusDamage(target);
            event.setAmount(event.getAmount() * multiplier);
    }
}
