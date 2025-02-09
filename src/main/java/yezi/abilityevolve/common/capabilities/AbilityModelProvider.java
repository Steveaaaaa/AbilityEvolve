package yezi.abilityevolve.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityModelProvider implements ICapabilitySerializable<CompoundTag> {
    private final AbilityModel abilityModel;

    private final LazyOptional<AbilityModel> optional;

    public AbilityModelProvider() {
        this.abilityModel = new AbilityModel();
        this.optional = LazyOptional.of(() -> abilityModel);
    }
    public AbilityModelProvider(AbilityModel abilityModel) {
        this.abilityModel = abilityModel;
        this.optional = LazyOptional.of(() -> abilityModel);
    }
    public void invalidate() {
        this.optional.invalidate();
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.ABILITY_MODEL_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        return this.abilityModel.serializeNBT();
    }
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.abilityModel.deserializeNBT(nbt);
    }
}
