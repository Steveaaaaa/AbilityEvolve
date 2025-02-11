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
import java.util.UUID;

public class AidSupportListener {
    private final Player player;
    public AidSupportListener(Player player){
        this.player = player;
    }
    private static final Map<UUID, Map<LivingEntity, HealTask>> healingTasks = new HashMap<>();

    public static void addHealingTask(Player player, LivingEntity entity, float healAmount) {
        healingTasks.computeIfAbsent(player.getUUID(), k -> new HashMap<>())
                .put(entity, new HealTask(entity, healAmount));
    }

    @SubscribeEvent
    public void onPlayerFeed(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() == this.player){
            ItemStack itemStack = event.getItemStack();
            AidSupportAbility.onItemUse(player, itemStack);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<Map.Entry<UUID, Map<LivingEntity, HealTask>>> playerIterator = healingTasks.entrySet().iterator();
        while (playerIterator.hasNext()) {
            Map.Entry<UUID, Map<LivingEntity, HealTask>> playerEntry = playerIterator.next();
            Iterator<Map.Entry<LivingEntity, HealTask>> taskIterator = playerEntry.getValue().entrySet().iterator();

            while (taskIterator.hasNext()) {
                Map.Entry<LivingEntity, HealTask> taskEntry = taskIterator.next();
                HealTask task = taskEntry.getValue();

                if (task.isCompleted()) {
                    taskIterator.remove();
                } else {
                    task.tick();
                }
            }

            if (playerEntry.getValue().isEmpty()) {
                playerIterator.remove();
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
