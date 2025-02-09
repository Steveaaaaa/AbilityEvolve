package yezi.abilityevolve.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.Config;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;

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

            Skill skill = Skill.values()[this.skill];
            assert player != null;
            int level = Config.getStartCost() + (ModCapabilities.getSkillModel(player).getSkillLevel(skill.index) - 1) * Config.getCostIncrease();
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
            if (ModCapabilities.getSkillModel(player).getSkillLevel(skill.index) < Config.getMaxLevel() && (player.totalExperience >= cost)) {
                player.giveExperiencePoints(-cost);
                ModCapabilities.getSkillModel(player).setSkillLevel(skill.index, ModCapabilities.getSkillModel(player).getSkillLevel(skill.index) + 1);
                for (int i = 0; i < ModCapabilities.getSkillModel(player).skillLevels.length; i++){
                    ModCapabilities.getAbilityModel(player).abilityPoint = (ModCapabilities.getAbilityModel(player).abilityPoint + ModCapabilities.getSkillModel(player).skillLevels[i] - ModCapabilities.getSkillModel(player).skillLevels.length)/Config.getAbilityPointIncrease();
                }
                SyncToClient.send(player);
                AbilitySyncToClient.send(player);
            }

        });
        context.get().setPacketHandled(true);
    }

    public static void send(Skill skill) {
        AbilityEvolve.NETWORK.sendToServer(new RequestLevelUp(skill));
    }
}
