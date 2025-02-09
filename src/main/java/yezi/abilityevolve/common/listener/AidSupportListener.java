package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.AidSupportAbility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AidSupportListener {
    private static final Map<LivingEntity, HealTask> healingTasks = new HashMap<>();
    public static void addHealingTask(LivingEntity entity, float healAmount) {
        healingTasks.put(entity, new HealTask(entity, healAmount));
    }

    @SubscribeEvent
    public void onPlayerFeed(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        AidSupportAbility.onItemUse(player, itemStack);
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<Map.Entry<LivingEntity, HealTask>> iterator = healingTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LivingEntity, HealTask> entry = iterator.next();
            HealTask task = entry.getValue();

            if (task.isCompleted()) {
                iterator.remove(); // 移除已完成的任务
            } else {
                task.tick();
            }
        }
    }

    private static class HealTask {
        private final LivingEntity entity;
        private final float healAmount;
        private int remainingTime = AidSupportAbility.DURATION_TICKS;
        private int tickCounter = 0;

        public HealTask(LivingEntity entity, float healAmount) {
            this.entity = entity;
            this.healAmount = healAmount;
        }

        public void tick() {
            if (!entity.isAlive()) {
                remainingTime = 0;
                return;
            }

            tickCounter++;
            if (tickCounter >= AidSupportAbility.HEAL_INTERVAL) {
                entity.heal(healAmount);
                tickCounter = 0;
            }

            remainingTime--;
        }

        public boolean isCompleted() {
            return remainingTime <= 0;
        }
    }
}