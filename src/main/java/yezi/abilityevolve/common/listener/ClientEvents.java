package yezi.abilityevolve.common.listener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import yezi.abilityevolve.client.Overlay;
import yezi.abilityevolve.client.screen.SkillScreen;
import yezi.abilityevolve.client.screen.buttons.KeyBinding;
import yezi.abilityevolve.common.particles.ParticleInit;

@EventBusSubscriber(modid = "abilityevolve", value = {Dist.CLIENT})

public class ClientEvents {
    public ClientEvents() {
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (KeyBinding.SKILLS_KEY.isDown()) {
            minecraft.setScreen(new SkillScreen());
        }
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("skill_page", new Overlay());
    }
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        ParticleInit.registerParticleFactories(event);
    }
}
