package yezi.skillablereforged.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class LeapStrikeNetworkHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("skillablereforged", "leapstrike"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.registerMessage(nextId(), LeapStrikePacket.class, LeapStrikePacket::encode, LeapStrikePacket::decode, LeapStrikePacket::handle);
    }
    public static <T> void sendToServer(T message) {
        LeapStrikeNetworkHandler.sendToServer(message);
    }
}
