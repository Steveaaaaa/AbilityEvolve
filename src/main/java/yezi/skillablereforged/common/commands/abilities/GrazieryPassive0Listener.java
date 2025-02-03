package yezi.skillablereforged.common.commands.abilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrazieryPassive0Listener {
    private final AidSupportAbility aidSupportAbility = new AidSupportAbility();

    @SubscribeEvent
    public void onPlayerFeedAnimal(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();

        // 调用 AidSupportAbility 处理喂食逻辑
        aidSupportAbility.onItemUse(player, itemStack);
    }
}