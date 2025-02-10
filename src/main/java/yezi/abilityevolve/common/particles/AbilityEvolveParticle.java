package yezi.abilityevolve.common.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yezi.abilityevolve.AbilityEvolve;

public class AbilityEvolveParticle {
    public static final DeferredRegister<ParticleType<?>> ABILITYEVOLVE_PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, "abilityevolve");

    public static final RegistryObject<SimpleParticleType> YELLOW_STAR =
            ABILITYEVOLVE_PARTICLES.register("yellow_star", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        AbilityEvolve.LOGGER.info("Registering particle types");
        ABILITYEVOLVE_PARTICLES.register(eventBus);
    }
}
