package yezi.abilityevolve.common.interfaces;

import net.minecraft.core.Direction;

public interface SpiderClimbing {
    boolean isClimbing();
    Direction getDirection();

    void setClimbing(boolean state, Direction dir);
}
