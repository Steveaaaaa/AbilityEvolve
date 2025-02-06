package yezi.skillablereforged.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import yezi.skillablereforged.common.abilities.LeapStrikeAbility;

public class LeapStrikeNetworkHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("skillablereforged", "leapstrike"),
            () -> "1.0",
            s -> true,
            s -> true
    );
    public static final LeapStrikeAbility MYINSTANCE = new LeapStrikeAbility();

    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.registerMessage(nextId(), LeapStrikePacket.class, LeapStrikePacket::encode, LeapStrikePacket::decode, LeapStrikePacket::handle);
        INSTANCE.registerMessage(nextId(), StartChargePacket.class, StartChargePacket::encode, StartChargePacket::decode, StartChargePacket::handle);
        INSTANCE.registerMessage(nextId(), ChargeTickPacket.class, ChargeTickPacket::encode, ChargeTickPacket::decode, ChargeTickPacket::handle);
        INSTANCE.registerMessage(nextId(), PerformLeapPacket.class, PerformLeapPacket::encode, PerformLeapPacket::decode, PerformLeapPacket::handle);
    }
    public static <T> void sendToServer(T message) {
        INSTANCE.sendToServer(message);
    }
}
