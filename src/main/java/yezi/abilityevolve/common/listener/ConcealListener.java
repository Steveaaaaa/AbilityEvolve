package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.ConcealAbility;
import yezi.abilityevolve.common.events.PlayerSoundEvent;

public class ConcealListener {
    @SubscribeEvent
    public static void onPlayerMakeSound(PlayerSoundEvent event) {
        if (PlayerTickListener.concealUnlockedMap.get(event.getPlayer().getUUID()))
            ConcealAbility.updateActionTime(event.getPlayer());
    }
    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (event.getEntity() instanceof Player &&  (PlayerTickListener.concealUnlockedMap.get(event.getEntity().getUUID()))) {
            ConcealAbility.updateActionTime(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player && PlayerTickListener.energeticUnlockedMap.get(event.getEntity().getUUID())) {
            ConcealAbility.updateActionTime(player);
        }

        if (event.getSource().getEntity() instanceof Player player && PlayerTickListener.concealUnlockedMap.get(event.getSource().getEntity().getUUID())) {
            ConcealAbility.updateActionTime(player);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player && PlayerTickListener.concealUnlockedMap.get(event.getSource().getEntity().getUUID())) {
            float multiplier = ConcealAbility.handleAttack(player);
            event.setAmount(event.getAmount() * multiplier);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (PlayerTickListener.concealUnlockedMap.get(event.getEntity().getUUID()))
            ConcealAbility.cleanupPlayer(event.getEntity());
    }
}
