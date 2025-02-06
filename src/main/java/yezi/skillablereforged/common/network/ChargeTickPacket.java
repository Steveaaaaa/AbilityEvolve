package yezi.skillablereforged.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;

import java.util.function.Supplier;

public class ChargeTickPacket {
    public static void encode(ChargeTickPacket msg, FriendlyByteBuf buf) {}
    public static ChargeTickPacket decode(FriendlyByteBuf buf) { return new ChargeTickPacket(); }

    public static void handle(ChargeTickPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                LeapStrikeAbility ability = new LeapStrikeAbility();
                ability.chargeTick(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
