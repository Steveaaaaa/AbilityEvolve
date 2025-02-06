package yezi.skillablereforged.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;

import java.util.function.Supplier;

public class PerformLeapPacket {
    public static void encode(PerformLeapPacket msg, FriendlyByteBuf buf) {}
    public static PerformLeapPacket decode(FriendlyByteBuf buf) { return new PerformLeapPacket(); }

    public static void handle(PerformLeapPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                LeapStrikeAbility ability = new LeapStrikeAbility();
                    ability.performLeap(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}