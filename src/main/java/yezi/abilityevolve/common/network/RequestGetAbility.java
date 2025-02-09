package yezi.abilityevolve.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.abilities.*;

import java.util.function.Supplier;

public class RequestGetAbility {
    private final String name;
    private final int index;

    public RequestGetAbility(Ability ability) {
        this.name = ability.name;
        this.index = ability.index;
    }
    public RequestGetAbility(FriendlyByteBuf buffer) {
        this.name = buffer.readUtf();
        this.index = buffer.readInt();
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.name);
        buffer.writeInt(this.index);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            assert player != null;
            AbilityManager abilityManager = new AbilityManager(player);
            Ability ability = getAbilityByName(this.name);
            abilityManager.getAbility(ability.abilityType, ability.index, ability.skillPointCost);
            AbilitySyncToClient.send(player);
        });
        context.get().setPacketHandled(true);
    }
    private Ability getAbilityByName(String name){
        return switch (name) {
            case "aid_support" -> new AidSupportAbility();
            case "concentrated_feeding" -> new ConcentratedFeedingAbility();
            case "wolves" -> new WolvesAbility();
            default -> throw new IllegalArgumentException("Invalid ability name");
        };
    }
    public static void send(Ability ability){
        AbilityEvolve.NETWORK.sendToServer(new RequestGetAbility(ability));
    }
}