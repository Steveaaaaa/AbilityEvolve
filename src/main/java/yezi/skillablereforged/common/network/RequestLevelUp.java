package yezi.skillablereforged.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.Skillablereforged;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.commands.skills.Skill;

import java.util.function.Supplier;

public class RequestLevelUp {
    private final int skill;

    public RequestLevelUp(Skill skill) {
        this.skill = skill.index;
    }

    public RequestLevelUp(FriendlyByteBuf buffer) {
        this.skill = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.skill);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();

            assert player != null;

            SkillModel skillModel = SkillModel.get(player);
            Skill skill = Skill.values()[this.skill];
            int level = Config.getStartCost() + (skillModel.getSkillLevel(skill) - 1) * Config.getCostIncrease();
            int cost;
            if (level >= 0 && level <= 16) {
                cost = level * level + 6 * level;
            } else if (level >= 17 && level <= 31) {
                cost = (int) (2.5 * level * level - 40.5 * level + 360);
            } else if (level >= 32) {
                cost = (int) (4.5 * level * level - 162.5 * level + 2220);
            } else {
                throw new IllegalArgumentException("Invalid skill level: " + level);
            }
            if (skillModel.getSkillLevel(skill) < Config.getMaxLevel() && (player.totalExperience >= cost)) {
                player.giveExperiencePoints(-cost);
                skillModel.increaseSkillLevel(skill);
                SyncToClient.send(player);
            }

        });
        context.get().setPacketHandled(true);
    }

    public static void send(Skill skill) {
        Skillablereforged.NETWORK.sendToServer(new RequestLevelUp(skill));
    }
}
