package yezi.abilityevolve.common.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class YellowStarParticle extends TextureSheetParticle {
    public YellowStarParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        super(level, x, y, z, dx, dy, dz);
        this.setSize(1.5F, 1.5F);
        this.lifetime = 20 + this.random.nextInt(10);
        this.gravity = 0.0F;
        this.alpha = 1.0F;
        this.quadSize = 0.6F + this.random.nextFloat() * 0.2F;

        this.setColor(1.0F, 0.9F + this.random.nextFloat() * 0.1F, 0.3F);

        this.xd = dx * 0.1;
        this.yd = dy * 0.1 + 0.005;
        this.zd = dz * 0.1;
    }

    @Override
    public void tick() {
        super.tick();

        this.yd += 0.005;

        this.alpha = Math.max(0.0F, 1.0F - (float) this.age / (float) this.lifetime);

        this.quadSize = (0.5F + (float) Math.sin((double) this.age / this.lifetime * Math.PI)) * 0.6F;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            YellowStarParticle particle = new YellowStarParticle(level, x, y, z, dx, dy, dz);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}
