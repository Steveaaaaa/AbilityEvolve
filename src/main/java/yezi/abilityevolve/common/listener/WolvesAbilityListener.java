package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.WolvesAbility;
import java.util.Random;

public class WolvesAbilityListener {
    private final Player player;
    private final WolvesAbility wolvesAbility;
    public WolvesAbilityListener(Player player) {
        this.player = player;
        this.wolvesAbility = new WolvesAbility();
    }
    private static final Random random = new Random();

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onWolfAttack(LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof Wolf wolf)) return;
        if (!wolf.isTame() || !(wolf.getOwner() == this.player)) return;
        wolvesAbility.applyEffect(wolf);
    }
    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        if (target instanceof Wolf) {
            int dodgePercent = WolvesAbility.DODGE_PERCENT[wolvesAbility.abilityLevel];
            if (random.nextInt(100) < dodgePercent) {
                event.setCanceled(true);
            }
        }
    }
}
