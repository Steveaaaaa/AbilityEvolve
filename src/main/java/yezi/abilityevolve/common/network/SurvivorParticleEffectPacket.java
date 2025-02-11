package yezi.abilityevolve.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.utils.ParticleSpawner;

import java.util.function.Supplier;

public class SurvivorParticleEffectPacket {
    private final Vec3 playerPos;   // 玩家位置
    private final Vec3 attackerPos; // 攻击者位置
    private final String message;   // 自定义消息

    public SurvivorParticleEffectPacket(Vec3 playerPos, Vec3 attackerPos, String message) {
        this.playerPos = playerPos;
        this.attackerPos = attackerPos;
        this.message = message;
    }

    public static void encode(SurvivorParticleEffectPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.message);
        buf.writeDouble(msg.playerPos.x());
        buf.writeDouble(msg.playerPos.y());
        buf.writeDouble(msg.playerPos.z());
        buf.writeDouble(msg.attackerPos.x());
        buf.writeDouble(msg.attackerPos.y());
        buf.writeDouble(msg.attackerPos.z());
    }

    public static SurvivorParticleEffectPacket decode(FriendlyByteBuf buf) {
        String message = buf.readUtf();
        Vec3 playerPos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3 attackerPos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());

        return new SurvivorParticleEffectPacket(playerPos, attackerPos, message);
    }

    public static void handle(SurvivorParticleEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // 使用解码后的位置信息调用特效生成方法
            ParticleSpawner.spawnResistanceEffect(msg.playerPos, msg.attackerPos);
            // 打印消息到控制台
            System.out.println(msg.message);
        });
        context.setPacketHandled(true);
    }
    public static void sendToAllClients(Vec3 playerPos, Vec3 attackerPos, String message) {
        SurvivorParticleEffectPacket packet = new SurvivorParticleEffectPacket(playerPos, attackerPos, message);
        AbilityEvolve.NETWORK.send(PacketDistributor.ALL.noArg(), packet);
        AbilityEvolve.LOGGER.info("SurvivorParticleEffectPacket sent to all clients.");
    }
}



