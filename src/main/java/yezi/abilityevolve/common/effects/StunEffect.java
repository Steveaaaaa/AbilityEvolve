package yezi.abilityevolve.common.effects;

import net.minecraft.world.entity.LivingEntity;
import yezi.abilityevolve.common.capabilities.ModCapabilities;

public class StunEffect {
    public static void apply(LivingEntity entity, double durationSeconds) {
        if (entity == null || entity.isRemoved()) return;

        entity.getCapability(ModCapabilities.STUN).ifPresent(stun -> stun.applyStun(entity, durationSeconds));
    }
}
