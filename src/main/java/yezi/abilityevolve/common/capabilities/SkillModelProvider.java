package yezi.abilityevolve.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SkillModelProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final SkillModel skillModel = new SkillModel();
    private final LazyOptional<SkillModel> optional = LazyOptional.of(() -> skillModel);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.SKILL_MODEL_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return skillModel.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        skillModel.deserializeNBT(nbt);
    }
}
