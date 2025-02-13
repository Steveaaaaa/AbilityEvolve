package yezi.abilityevolve.common.abilities;

import net.minecraft.core.Direction;
import yezi.abilityevolve.common.interfaces.SpiderClimbing;

public class SpiderClimbingImpl implements SpiderClimbing {
    private boolean climbing;
    private Direction direction = Direction.UP;

    @Override
    public boolean isClimbing() {
        return climbing;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setClimbing(boolean state, Direction dir) {
        this.climbing = state;
        this.direction = dir;
    }
}
