package yezi.skillablereforged.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.capabilities.SkillModel;

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

  //  public SyncToClient(CompoundTag abilityModel){this.abilityModel = abilityModel;}

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> SkillModel.get().deserializeNBT(this.skillModel));
        context.get().setPacketHandled(true);
    }

    public static void send(Player player) {
        Skillablereforged.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new SyncToClient(SkillModel.get(player).serializeNBT()));
    }
}