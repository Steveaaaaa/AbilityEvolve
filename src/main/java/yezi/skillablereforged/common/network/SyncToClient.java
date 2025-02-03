package yezi.skillablereforged.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.capabilities.AbilityModel;
import yezi.skillablereforged.common.capabilities.SkillModel;

import java.util.function.Supplier;

public class SyncToClient {
    private final CompoundTag skillModel;

    private final CompoundTag abilityModel;

    public SyncToClient(CompoundTag skillModel, CompoundTag abilityModel) {
        this.skillModel = skillModel;
        this.abilityModel = abilityModel;
    }

    public SyncToClient(FriendlyByteBuf buffer) {
        this.skillModel = buffer.readNbt();
        this.abilityModel = buffer.readNbt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.skillModel);
        buffer.writeNbt(this.abilityModel);
    }

  //  public SyncToClient(CompoundTag abilityModel){this.abilityModel = abilityModel;}

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            SkillModel.get().deserializeNBT(this.skillModel);
            AbilityModel.get().deserializeNBT(this.abilityModel);
        });
        context.get().setPacketHandled(true);
    }

    public static void send(Player player) {
        Skillablereforged.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new SyncToClient(SkillModel.get(player).serializeNBT(), AbilityModel.get(player).serializeNBT()));
    }
}