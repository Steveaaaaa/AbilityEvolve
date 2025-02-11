package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.ExposeWeaknessAbility;

public class ExposeWeaknessListener {
    private final Player player;
    private final ExposeWeaknessAbility exposeWeaknessAbility;
    public ExposeWeaknessListener(Player player) {
        this.player = player;
        exposeWeaknessAbility = new ExposeWeaknessAbility();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onEntityDamage(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() == this.player)) return;
        LivingEntity target = event.getEntity();
        exposeWeaknessAbility.applyWeaknessMark(target, player, event.getAmount());
    }
}

