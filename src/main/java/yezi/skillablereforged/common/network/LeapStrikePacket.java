package yezi.skillablereforged.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;

import java.util.UUID;
import java.util.function.Supplier;

public record LeapStrikePacket(UUID playerId, boolean isCharging) {

    public static void encode(LeapStrikePacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId);
        buf.writeBoolean(msg.isCharging);
    }

    public static LeapStrikePacket decode(FriendlyByteBuf buf) {
        return new LeapStrikePacket(buf.readUUID(), buf.readBoolean());
    }

    public static void handle(LeapStrikePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                LeapStrikeAbility ability = new LeapStrikeAbility();
                if (msg.isCharging) {
                    ability.startCharging(player);
                } else {
                    ability.performLeap(player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
