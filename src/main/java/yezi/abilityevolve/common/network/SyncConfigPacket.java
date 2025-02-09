package yezi.abilityevolve.common.network;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.config.SkillLockLoader;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

@EventBusSubscriber(Dist.CLIENT)
public class SyncConfigPacket {
    private static final Logger LOGGER = Logger.getLogger(SyncConfigPacket.class.getName());
    private static final Gson GSON = new Gson();

    private final Map<String, Requirement[]> skillLocks;
    private final Map<String, Requirement[]> craftSkillLocks;
    private final Map<String, Requirement[]> attackSkillLocks;

    public SyncConfigPacket(Map<String, Requirement[]> skillLocks, Map<String, Requirement[]> craftSkillLocks, Map<String, Requirement[]> attackSkillLocks) {
        this.skillLocks = skillLocks;
        this.craftSkillLocks = craftSkillLocks;
        this.attackSkillLocks = attackSkillLocks;
        LOGGER.info("SyncConfigPacket created.");
    }

    public SyncConfigPacket(FriendlyByteBuf buf) {
        Type type = SkillLockLoader.getSkillLocksType();
        this.skillLocks = GSON.fromJson(buf.readUtf(), type);
        this.craftSkillLocks = GSON.fromJson(buf.readUtf(), type);
        this.attackSkillLocks = GSON.fromJson(buf.readUtf(), type);
        LOGGER.info("SyncConfigPacket received from buffer.");
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(GSON.toJson(this.skillLocks));
        buf.writeUtf(GSON.toJson(this.craftSkillLocks));
        buf.writeUtf(GSON.toJson(this.attackSkillLocks));
        LOGGER.info("SyncConfigPacket written to buffer.");
    }

    public static void handle(SyncConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true); // 先标记包已处理，避免并发问题
        ctx.get().enqueueWork(() -> {
            SkillLockLoader.setSkillLocks(msg.skillLocks);
            SkillLockLoader.setCraftSkillLocks(msg.craftSkillLocks);
            SkillLockLoader.setAttackSkillLocks(msg.attackSkillLocks);
            LOGGER.info("Skill configuration updated on the client.");

            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player != null) {
                minecraft.execute(() -> {
                    LOGGER.info("Refreshing tooltips due to configuration update.");
                    player.displayClientMessage(Component.literal("Skill configuration updated!"), true);
                });
            }
        });
    }

    public static void sendToAllClients() {
        SyncConfigPacket packet = new SyncConfigPacket(
                SkillLockLoader.getSkillLocks(),
                SkillLockLoader.getCraftSkillLocks(),
                SkillLockLoader.getAttackSkillLocks()
        );
        AbilityEvolve.NETWORK.send(PacketDistributor.ALL.noArg(), packet);
        LOGGER.info("SyncConfigPacket sent to all clients.");
    }
}

