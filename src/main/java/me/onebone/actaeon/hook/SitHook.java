package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import me.onebone.actaeon.entity.EntityTameable;

/**
 * @author CreeperFace
 */
public class SitHook extends MovingEntityHook {

    private EntityTameable tameable;

    private boolean sitting;

    public SitHook(EntityTameable entity) {
        super(entity);
        this.tameable = entity;
        this.setCompatibility(FLAG_MOVEMENT | FLAG_BUSY);
    }

    @Override
    public boolean shouldExecute() {
        if (!tameable.isTamed() || !tameable.onGround) {
            return false;
        }

        Player owner = tameable.getOwner();

        return owner == null /*|| tameable.distanceSquared(owner) > 144*/ || sitting;
    }

    @Override
    public void startExecuting() {
        tameable.getRoute().forceStop();
        tameable.setSitting(true);
    }

    @Override
    public void reset() {
        tameable.setSitting(false);
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }
}
