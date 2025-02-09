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

public class SyncToClient {
    private final CompoundTag skillModel;
    public SyncToClient(CompoundTag skillModel) {
        this.skillModel = skillModel;
    }

    public SyncToClient(FriendlyByteBuf buffer) {
        this.skillModel = buffer.readNbt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.skillModel);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(ModCapabilities.SKILL_MODEL_CAPABILITY).ifPresent(skillModel -> {
                    skillModel.deserializeNBT(this.skillModel);
                });
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void send(Player player) {
        AbilityEvolve.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new SyncToClient(ModCapabilities.getSkillModel(player).serializeNBT()));
    }
}