package yezi.skillablereforged.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.client.Overlay;
import yezi.skillablereforged.common.commands.skills.RequirementType;

import java.util.function.Supplier;

public class NotifyWarning {
    private final ResourceLocation resource;
    private final RequirementType type;

    public NotifyWarning(ResourceLocation resource, RequirementType type) {
        this.resource = resource;
        this.type = type;
    }

    public NotifyWarning(FriendlyByteBuf buffer) {
        this.resource = buffer.readResourceLocation();
        this.type = buffer.readEnum(RequirementType.class);
    }

    public void encode(FriendlyByteBuf buffer) {
        // 写入缓冲区
        buffer.writeResourceLocation(this.resource);
        buffer.writeEnum(this.type);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Overlay.showWarning(this.resource, this.type);
            }

        });
        context.get().setPacketHandled(true);
    }

    public static void send(Player player, ResourceLocation resource, RequirementType type) {
        Skillablereforged.NETWORK.send(PacketDistributor.PLAYER.with(() -> {
            return (ServerPlayer)player;
        }), new NotifyWarning(resource, type));
    }
}