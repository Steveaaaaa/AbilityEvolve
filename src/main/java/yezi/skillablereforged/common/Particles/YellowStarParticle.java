package yezi.skillablereforged.common.Particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class YellowStarParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public YellowStarParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;

        this.lifetime = 20 + this.random.nextInt(10);
        this.quadSize = 0.1F + this.random.nextFloat() * 0.2F;
        this.setSpriteFromAge(sprites);
        this.gravity = 0.0F;

        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}