package yezi.skillablereforged.common;

import yezi.skillablereforged.common.capabilities.SkillModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class CuriosCompat {
    public CuriosCompat() {
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void onChangeCurio(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (!player.isCreative()) {
                ItemStack item = event.getTo();
                if (!SkillModel.get(player).canUseItem(player, item)) {
                    player.drop(item.copy(), false);
                    item.setCount(0);
                }
            }
        }
    }
}
