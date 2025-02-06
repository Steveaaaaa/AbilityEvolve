package yezi.skillablereforged.common.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.common.abilities.ExposeWeaknessAbility;

public class ExposeWeaknessListener {
    private static final ExposeWeaknessAbility exposeWeaknessAbility = new ExposeWeaknessAbility();
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onEntityDamage(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player attacker)) return;
        LivingEntity target = event.getEntity();
        exposeWeaknessAbility.applyWeaknessMark(target, attacker, event.getAmount());
    }
}
