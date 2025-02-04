package yezi.skillablereforged.common.network;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.skills.Requirement;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

@EventBusSubscriber({Dist.CLIENT})
public class SyncSkillConfigPacket {
    private static final Logger LOGGER = Logger.getLogger(SyncSkillConfigPacket.class.getName());
    private final Map<String, Requirement[]> skillLocks;
    private final Map<String, Requirement[]> craftSkillLocks;
    private final Map<String, Requirement[]> attackSkillLocks;
 //   private final Map<String, Requirement[]> abilityLocks;

    public SyncSkillConfigPacket(Map<String, Requirement[]> skillLocks, Map<String, Requirement[]> craftSkillLocks, Map<String, Requirement[]> attackSkillLocks) {
        this.skillLocks = skillLocks;
        this.craftSkillLocks = craftSkillLocks;
        this.attackSkillLocks = attackSkillLocks;
   //     this.abilityLocks = abilityLocks;
        LOGGER.info("SyncSkillConfigPacket created with skillLocks: " + skillLocks + ", craftSkillLocks: " + craftSkillLocks + ", attackSkillLocks: " + attackSkillLocks);
    }

    public SyncSkillConfigPacket(FriendlyByteBuf buf) {
        String skillLocksJson = buf.readUtf();
        String craftSkillLocksJson = buf.readUtf();
        String attackSkillLocksJson = buf.readUtf();
    //    String abilityLocksJson = buf.readUtf();
        Type type = Config.getSkillLocksType();
        this.skillLocks = (Map)(new Gson()).fromJson(skillLocksJson, type);
        this.craftSkillLocks = (Map)(new Gson()).fromJson(craftSkillLocksJson, type);
        this.attackSkillLocks = (Map)(new Gson()).fromJson(attackSkillLocksJson, type);
    //    this.abilityLocks = (Map)(new Gson()).fromJson(abilityLocksJson, type);
        LOGGER.info("SyncSkillConfigPacket read from buffer with skillLocks: " + this.skillLocks + ", craftSkillLocks: " + this.craftSkillLocks + ", attackSkillLocks: " + this.attackSkillLocks);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf((new Gson()).toJson(this.skillLocks));
        buf.writeUtf((new Gson()).toJson(this.craftSkillLocks));
        buf.writeUtf((new Gson()).toJson(this.attackSkillLocks));
      //  buf.writeUtf((new Gson()).toJson(this.abilityLocks));
        LOGGER.info("SyncSkillConfigPacket written to buffer with skillLocks: " + this.skillLocks + ", craftSkillLocks: " + this.craftSkillLocks + ", attackSkillLocks: " + this.attackSkillLocks);
    }

    public static void handle(SyncSkillConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Config.setSkillLocks(msg.skillLocks);
            Config.setCraftSkillLocks(msg.craftSkillLocks);
            Config.setAttackSkillLocks(msg.attackSkillLocks);
    //        Config.setAbilityLocks(msg.abilityLocks);
            LOGGER.info("Skill configuration updated on the client.");
            Minecraft.getInstance().execute(() -> {
                LOGGER.info("Refreshing tooltips due to configuration update.");
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(Component.literal("Skill configuration updated!"),true);
                }

            });
        });
        ctx.get().setPacketHandled(true);
        LOGGER.info("SyncSkillConfigPacket handled on the client.");
    }

    public static void sendToAllClients() {
        Map<String, Requirement[]> skillLocks = Config.getSkillLocks();
        Map<String, Requirement[]> craftSkillLocks = Config.getCraftSkillLocks();
        Map<String, Requirement[]> attackSkillLocks = Config.getAttackSkillLocks();
   //     Map<String, Requirement[]> abilityLocks = Config.getAbilityLocks();
        SyncSkillConfigPacket packet = new SyncSkillConfigPacket(skillLocks, craftSkillLocks, attackSkillLocks);
        Skillablereforged.NETWORK.send(PacketDistributor.ALL.noArg(), packet);
        LOGGER.info("Sent SyncSkillConfigPacket to all clients.");
    }
}
