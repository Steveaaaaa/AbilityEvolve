package yezi.abilityevolve.common.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import yezi.abilityevolve.common.skills.SkillRequirementChecker;

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
                if (!SkillRequirementChecker.canUseItem(player, item)) {
                    player.drop(item.copy(), false);
                    item.setCount(0);
                }
            }
        }
    }
}
