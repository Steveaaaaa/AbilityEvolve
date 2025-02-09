package yezi.abilityevolve.common.Particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class YellowStarParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprite;

    public YellowStarParticleProvider(SpriteSet sprite) {
        this.sprite = sprite;
    }

    @Override
    public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        return new YellowStarParticle(level, x, y, z, dx, dy, dz, this.sprite);
    }
}
