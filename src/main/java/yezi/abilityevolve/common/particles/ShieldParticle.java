package yezi.abilityevolve.common.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ShieldParticle extends TextureSheetParticle {
    private float initialAlpha;
    public ShieldParticle(ClientLevel world, double x, double y, double z, double dx, double dy, double dz, ShieldParticleData data) {
        super(world, x, y, z, dx, dy, dz);
        this.gravity = 0.0F;
        this.xd = dx * 0.05;
        this.yd = 0.0;
        this.zd = dz * 0.05;
        this.lifetime = 2;
        this.initialAlpha = data.getAlpha();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        this.alpha = this.initialAlpha * (1.0f - (this.age / (float) this.lifetime));
        super.render(buffer, renderInfo, partialTicks);
    }
    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    public static class Provider implements ParticleProvider<ShieldParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(ShieldParticleData data, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            ShieldParticle particle = new ShieldParticle(world, x, y, z, dx, dy, dz, data);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
