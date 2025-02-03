package yezi.skillablereforged.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final AbilityModel abilityModel;

    private final LazyOptional<AbilityModel> optional;

    public AbilityProvider() {
        this.abilityModel = new AbilityModel();
        this.optional = LazyOptional.of(() -> abilityModel);
    }
    public AbilityProvider(AbilityModel abilityModel) {
        this.abilityModel = abilityModel;
        this.optional = LazyOptional.of(() -> abilityModel);
    }
    public void invalidate() {
        this.optional.invalidate();
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return capability == AbilityCapability.INSTANCE ? this.optional.cast() : LazyOptional.empty();
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
