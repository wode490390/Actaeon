package me.onebone.actaeon.hook;

import cn.nukkit.event.entity.EntityDamageEvent;
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

    public MovingEntityHook(MovingEntity entity) {
        this.entity = entity;
    }

    public MovingEntity getEntity() {
        return entity;
    }

    public void onUpdate(int tick) {
    }

    public void onDamage(EntityDamageEvent soure) {
    }

    public boolean shouldExecute() {
        return true;
    }
}
