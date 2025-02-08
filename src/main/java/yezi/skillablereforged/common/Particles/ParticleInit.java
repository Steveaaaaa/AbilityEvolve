package yezi.skillablereforged.common.Particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "skillablereforged");

    public static final RegistryObject<SimpleParticleType> YELLOW_STAR = PARTICLES.register("yellow_star",
            () -> new SimpleParticleType(false));
}
