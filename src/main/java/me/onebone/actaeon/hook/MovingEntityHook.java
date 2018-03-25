package me.onebone.actaeon.hook;

import me.onebone.actaeon.entity.MovingEntity;

/**
 * ECPlayerBotHook
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public abstract class MovingEntityHook {

    protected final MovingEntity entity;

    public boolean executing = false;
    private int compatibility;

    public MovingEntityHook(MovingEntity entity) {
        this.entity = entity;
    }

    public MovingEntity getEntity() {
        return entity;
    }

    public void onUpdate(int tick) {
    }

    public abstract boolean shouldExecute();

    public boolean canContinue() {
        return shouldExecute();
    }

    public void startExecuting() {

    }

    public void reset() {

    }

    public boolean isInterruptible() {
        return true;
    }

    public void setCompatibility(int bits) {
        this.compatibility = bits;
    }

    public int getCompatibility() {
        return compatibility;
    }
}
