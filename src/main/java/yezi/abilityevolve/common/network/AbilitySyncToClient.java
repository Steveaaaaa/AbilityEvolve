package yezi.abilityevolve.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.capabilities.ModCapabilities;

import java.util.function.Supplier;

public class AbilitySyncToClient {
    private final CompoundTag abilityModel;
    public AbilitySyncToClient(CompoundTag abilityModel) {
        this.abilityModel = abilityModel;
    }
    public AbilitySyncToClient(FriendlyByteBuf buffer) {
        this.abilityModel = buffer.readNbt();
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.abilityModel);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(ModCapabilities.ABILITY_MODEL_CAPABILITY).ifPresent(abilityModel -> {
                    abilityModel.deserializeNBT(this.abilityModel);
                });
            }
        });
        context.get().setPacketHandled(true);
    }
    public static void send(Player player) {
        AbilityEvolve.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new AbilitySyncToClient(ModCapabilities.getAbilityModel(player).serializeNBT()));
    }
}
