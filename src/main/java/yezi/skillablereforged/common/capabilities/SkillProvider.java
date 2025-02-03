package yezi.skillablereforged.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SkillProvider implements ICapabilitySerializable<CompoundTag> {
    private final SkillModel skillModel;
    private final LazyOptional<SkillModel> optional;

    public SkillProvider() {
        this.skillModel = new SkillModel();
        this.optional = LazyOptional.of(() -> this.skillModel);
    }

    public SkillProvider(SkillModel skillModel) {
        this.skillModel = skillModel;
        this.optional = LazyOptional.of(() -> skillModel);
    }

    public void invalidate() {
        this.optional.invalidate();
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return capability == SkillCapability.INSTANCE ? this.optional.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return this.skillModel.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.skillModel.deserializeNBT(nbt);
    }
}
