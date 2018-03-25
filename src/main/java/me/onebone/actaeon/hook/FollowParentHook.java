package me.onebone.actaeon.hook;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.AxisAlignedBB;
import me.onebone.actaeon.entity.EntityAgeable;
import me.onebone.actaeon.entity.animal.Animal;

/**
 * @author CreeperFace
 */
public class FollowParentHook extends MovingEntityHook {

    private final EntityAgeable ageable;
    private EntityAgeable parent;

    public FollowParentHook(EntityAgeable entity) {
        super(entity);

        this.ageable = entity;
    }

    @Override
    public boolean shouldExecute() {
        if (this.ageable.getGrowingAge() >= 0) {
            return false;
        }

        this.parent = findParent();
        return this.parent != null;
    }

    @Override
    public boolean canContinue() {
        double d;
        return this.ageable.getGrowingAge() < 0 && parent.isAlive() && (d = ageable.distanceSquared(parent)) >= 9 && d <= 256;
    }

    @Override
    public void onUpdate(int tick) {
        if (tick % 10 != 0)
            return;

        this.ageable.setTarget(parent, parent.getName(), true);
    }

    private EntityAgeable findParent() {
        AxisAlignedBB bb = ageable.getBoundingBox().grow(8, 4, 8);
        double dist = Double.MAX_VALUE;
        EntityAgeable target = null;

        for (Entity entity : ageable.getLevel().getNearbyEntities(bb, ageable)) {
            double l;
            if (entity.getClass() == ageable.getClass() && entity.isAlive() && ((EntityAgeable) entity).getGrowingAge() > 0 && (l = ageable.distanceSquared(entity)) < dist) {
                target = (Animal) entity;
                dist = l;
            }
        }

        return target;
    }
}
