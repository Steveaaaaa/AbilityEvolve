package yezi.abilityevolve.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

public class ShieldParticleData implements ParticleOptions {
    public static final Codec<ShieldParticleData> CODEC = Codec.FLOAT.xmap(ShieldParticleData::new, ShieldParticleData::getAlpha);
    public static final ParticleOptions.Deserializer<ShieldParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Override
        public ShieldParticleData fromCommand(ParticleType<ShieldParticleData> type, StringReader reader) {
            try {
                reader.expect(' ');
                float alpha = reader.readFloat();
                return new ShieldParticleData(alpha);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ShieldParticleData fromNetwork(ParticleType<ShieldParticleData> type, FriendlyByteBuf buffer) {
            return new ShieldParticleData(buffer.readFloat());
        }
    };
    private final float alpha;

    public ShieldParticleData(float alpha) {
        this.alpha = Mth.clamp(alpha, 0.0f, 1.0f);
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public ParticleType<?> getType() {
        return AbilityEvolveParticle.SHIELD.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(alpha);
    }

    @Override
    public String writeToString() {
        return String.format("shield_particle %.2f", alpha);
    }
}
