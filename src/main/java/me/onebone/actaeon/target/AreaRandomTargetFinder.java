package me.onebone.actaeon.target;

import cn.nukkit.Player;
import me.onebone.actaeon.entity.MovingEntity;

/**
 * Created by CreeperFace on 19.7.2017.
 */
public class AreaRandomTargetFinder extends TargetFinder {

    private final int radius;
    private boolean first = true;

    public AreaRandomTargetFinder(MovingEntity entity, long interval, int radius) {
        super(entity, interval);
        this.radius = radius;
    }

    @Override
    protected void find() {
        Player near = null;
        double nearest = this.radius * this.radius;

        for (Player player : this.getEntity().getLevel().getPlayers().values()) {
            if (this.getEntity().distanceSquared(player) < nearest) {
                near = player;
                nearest = this.getEntity().distance(player);
            }
        }

        if (near != null) {
            this.getEntity().setTarget(EntityTarget.builder().target(near).identifier(this.getEntity().getName()).build(), this.first);
            this.getEntity().setHate(near);
        } else {
            //this.getEntity().getRoute().forceStop();
            this.getEntity().resetMovementPath();
        }
        this.first = false;
    }
}
