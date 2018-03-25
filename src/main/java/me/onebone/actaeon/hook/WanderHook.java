package me.onebone.actaeon.hook;

import cn.nukkit.math.Vector3;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.route.AdvancedRouteFinder;

import java.util.Random;

/**
 * Created by CreeperFace on 19.7.2017.
 */
public class WanderHook extends MovingEntityHook {

    private Vector3 target;

    private double radius;
    private int chance;

    public WanderHook(MovingEntity entity) {
        this(entity, 60);
    }

    public WanderHook(MovingEntity entity, int chance) {
        this(entity, chance, 10);
    }

    public WanderHook(MovingEntity entity, int chance, double radius) {
        super(entity);
        this.radius = radius;
        this.chance = chance;
        setCompatibility(0b1);
    }

    @Override
    public boolean shouldExecute() {
        /*if(this.entity.getAge() >= 100) {
            return false;
        }*/

        if (entity.getLevel().rand.nextInt(chance) != 0)
            return false;

        Vector3 vec = findVector();

        if (vec == null)
            return false;

        target = vec;
        return true;
    }

    @Override
    public boolean canContinue() {
        return this.entity.getRoute().hasRoute() && !this.entity.getRoute().hasArrived();
    }

    @Override
    public void startExecuting() {
        this.entity.setTarget(this.target, this.entity.getName(), true);
    }

    private Vector3 findVector() {
        Random random = this.entity.level.rand;
        Vector3 base = this.entity.getRealTarget() != null ? this.entity.getTarget() : this.getEntity().getPosition();

        double r = random.nextDouble() * 360;
        double x = this.radius * Math.cos(Math.toRadians(r));
        double z = this.radius * Math.sin(Math.toRadians(r));
        double y = base.getY();

        Vector3 highest = ((AdvancedRouteFinder) this.getEntity().getRoute()).getHighestUnder(x, y + 2, z, 7);
        if (highest == null)
            return null;
        y = highest.getY() + 1;

        return new Vector3(base.getX() + x, y, base.getZ() + z);
    }
}
