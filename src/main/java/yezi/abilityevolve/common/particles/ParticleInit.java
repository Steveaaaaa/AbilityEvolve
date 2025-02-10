package yezi.abilityevolve.common.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, "abilityevolve");

    public static final RegistryObject<SimpleParticleType> YELLOW_STAR = register("yellow_star", true);

    private static RegistryObject<SimpleParticleType> register(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new SimpleParticleType(alwaysShow));
    }

    public static void register(IEventBus modEventBus) {
        PARTICLES.register(modEventBus);
    }

    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(YELLOW_STAR.get(), YellowStarParticle.Provider::new);
    }
}