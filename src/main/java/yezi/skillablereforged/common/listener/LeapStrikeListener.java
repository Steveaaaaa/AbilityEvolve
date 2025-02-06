package yezi.skillablereforged.common.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;
import yezi.skillablereforged.common.network.ChargeTickPacket;
import yezi.skillablereforged.common.network.LeapStrikeNetworkHandler;
import yezi.skillablereforged.common.network.PerformLeapPacket;
import yezi.skillablereforged.common.network.StartChargePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeapStrikeListener {
    private static final LeapStrikeAbility ability = new LeapStrikeAbility();
    private static final Map<UUID, Boolean> isCharging = new HashMap<>();
  //  private double previousY = 0.0;
  //  private boolean wasInAir = false;
    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null) return;
        LocalPlayer player = Minecraft.getInstance().player;
        UUID uuid = player.getUUID();
        if (Minecraft.getInstance().options.keyJump.isDown()) {
            if (!isCharging.containsKey(uuid) || !isCharging.get(uuid)) {
                isCharging.put(uuid, true);
                LeapStrikeNetworkHandler.sendToServer(new StartChargePacket());
            } else {
                LeapStrikeNetworkHandler.sendToServer(new ChargeTickPacket());
            }
        } else if (isCharging.containsKey(uuid) && isCharging.get(uuid)) {
            isCharging.put(uuid, false);
            LeapStrikeNetworkHandler.sendToServer(new PerformLeapPacket());
        }
    }
    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (player.onGround()){
            ability.onPlayerLand(player);
            return;
        }
        LivingEntity target = event.getEntity();
        ability.triggerLeapStrike(player, target);
    }
   /* @SubscribeEvent
    public void onPlayerLand(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.onGround()) {
            if (wasInAir) {
                ability.onPlayerLand((ServerPlayer) player);
                wasInAir = false;
            }
        } else {
            wasInAir = true;
        }
    }*/
}
