package yezi.abilityevolve.client;

import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.capabilities.SkillModel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientUtils {
    public ClientUtils() {
    }

    public static SkillModel getClientSkillModel() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            return player.getCapability(ModCapabilities.SKILL_MODEL_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("Player does not have a Skill Model!"));
        } else {
            throw new IllegalStateException("Minecraft client player is null!");
        }
    }
}