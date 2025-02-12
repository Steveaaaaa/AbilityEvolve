package yezi.abilityevolve.common.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yezi.abilityevolve.AbilityEvolve;

@Mod.EventBusSubscriber(modid = "abilityevolve", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AbilityEvolveParticleRegister {
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        AbilityEvolve.LOGGER.info("Registering particle factories");
        event.registerSpriteSet(AbilityEvolveParticle.YELLOW_STAR.get(), YellowStarParticle.Provider::new);
        event.registerSpriteSet((ParticleType<ShieldParticleData>)AbilityEvolveParticle.SHIELD.get(), ShieldParticle.Provider::new);
    }
}
