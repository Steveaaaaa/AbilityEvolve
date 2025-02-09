package yezi.abilityevolve.client;

import com.mojang.blaze3d.platform.InputConstants;
import yezi.abilityevolve.client.screen.buttons.KeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "AbilityEvolve", value = {Dist.CLIENT}, bus = Bus.MOD)
public class Keybind {
    public static final KeyMapping openKey;

    public Keybind() {
    }

    @SubscribeEvent
    public static void keybind(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.SKILLS_KEY);
    }

    static {
        openKey = new KeyMapping("key.skills", KeyConflictContext.IN_GAME, InputConstants.getKey(71, -1), "key.AbilityEvolve.category");
    }
}
