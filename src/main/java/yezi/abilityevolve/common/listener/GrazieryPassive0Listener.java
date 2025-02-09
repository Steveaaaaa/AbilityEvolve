package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.AidSupportAbility;

public class GrazieryPassive0Listener {
    private final AidSupportAbility aidSupportAbility = new AidSupportAbility();

    @SubscribeEvent
    public void onPlayerFeed(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        aidSupportAbility.onItemUse(player, itemStack);
    }
}