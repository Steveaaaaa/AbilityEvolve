package yezi.abilityevolve.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import yezi.abilityevolve.common.interfaces.IStunCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StunCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final LazyOptional<IStunCapability> instance = LazyOptional.of(StunCapability::new);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.STUN ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag(); // 可选：存储眩晕数据
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // 可选：加载眩晕数据
    }
}

