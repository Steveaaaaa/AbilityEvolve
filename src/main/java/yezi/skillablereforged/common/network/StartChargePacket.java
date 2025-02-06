package yezi.skillablereforged.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;

import java.util.function.Supplier;

public class StartChargePacket {
    public static void encode(StartChargePacket msg, FriendlyByteBuf buf) {}
    public static StartChargePacket decode(FriendlyByteBuf buf) { return new StartChargePacket(); }

    public static void handle(StartChargePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                LeapStrikeAbility ability = new LeapStrikeAbility();
                ability.startCharging(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
