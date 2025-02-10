package yezi.abilityevolve.common.listener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.AbilityEvolve;
import yezi.abilityevolve.client.Overlay;
import yezi.abilityevolve.client.screen.SkillScreen;
import yezi.abilityevolve.client.screen.buttons.KeyBinding;

//@Mod.EventBusSubscriber(modid = "abilityevolve", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    public ClientEvents() {
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        AbilityEvolve.LOGGER.info("Key input event: {}", event.getKey());
        if (KeyBinding.SKILLS_KEY.isDown()) {
            minecraft.setScreen(new SkillScreen());
        }
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        AbilityEvolve.LOGGER.info("Registering overlays");
        event.registerAboveAll("skill_page", new Overlay());
    }
   /* @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        AbilityEvolve.LOGGER.info("Registering particle factories");
        event.registerSpriteSet(AbilityEvolveParticle.YELLOW_STAR.get(), YellowStarParticle.Provider::new);
    }*/
}
