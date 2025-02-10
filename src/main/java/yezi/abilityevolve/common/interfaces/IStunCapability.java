package yezi.abilityevolve.common.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface IStunCapability {
    boolean isStunned();
    void applyStun(LivingEntity entity, double durationSeconds);
    void removeStun(LivingEntity entity);
}
