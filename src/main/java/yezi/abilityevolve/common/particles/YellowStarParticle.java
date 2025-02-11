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
        this.setSize(0.6F, 0.6F);
        this.lifetime = 1;
        this.gravity = 0.0F;
        this.alpha = 1.0F;
        this.quadSize = 0.2F;
        this.xd = dx * 0.05;
        this.yd = 0.0;
        this.zd = dz * 0.05;
    }

    @Override
    public void tick() {
        super.tick();

        this.alpha = Math.max(0.0F, 1.0F - (float) this.age / (float) this.lifetime);

     //   this.quadSize = (0.5F + (float) Math.sin((double) this.age / this.lifetime * Math.PI)) * 0.6F;
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
