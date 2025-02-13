package yezi.abilityevolve.common.network;


import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.capabilities.ModCapabilities;

import java.util.Objects;
import java.util.function.Supplier;

public class ClimbingSyncPacket {
    private final boolean isClimbing;
    private final Direction direction;

    public ClimbingSyncPacket(boolean isClimbing, Direction direction) {
        this.isClimbing = isClimbing;
        this.direction = direction;
    }

    public static void encode(ClimbingSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isClimbing);
        buf.writeEnum(msg.direction);
    }

    public static ClimbingSyncPacket decode(FriendlyByteBuf buf) {
        return new ClimbingSyncPacket(
                buf.readBoolean(),
                buf.readEnum(Direction.class)
        );
    }

    public static void handle(ClimbingSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer sender = ctx.get().getSender();
                if (sender != null) {
                    ModCapabilities.getClimbing(sender).setClimbing(msg.isClimbing, msg.direction);

                    sendToAllTracking(new ClimbingSyncPacket(msg.isClimbing, msg.direction), sender);
                }
            } else {
                NetworkEvent.Context context = ctx.get();
                  ModCapabilities.getClimbing(Objects.requireNonNull(context.getSender())).setClimbing(msg.isClimbing, msg.direction);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    public static void sendToServer(ClimbingSyncPacket packet) {
        AbilityEvolve.NETWORK.sendToServer(packet);
    }

    public static void sendToAllTracking(ClimbingSyncPacket packet, ServerPlayer player) {
        AbilityEvolve.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), packet);
    }
}

