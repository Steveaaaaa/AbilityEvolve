package yezi.skillablereforged.client;

import yezi.skillablereforged.common.capabilities.SkillCapability;
import yezi.skillablereforged.common.capabilities.SkillModel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientUtils {
    public ClientUtils() {
    }

    public static SkillModel getClientSkillModel() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            return (SkillModel)player.getCapability(SkillCapability.INSTANCE).orElseThrow(() -> {
                return new IllegalArgumentException("Player does not have a Skill Model!");
            });
        } else {
            throw new IllegalStateException("Minecraft client player is null!");
        }
    }
}