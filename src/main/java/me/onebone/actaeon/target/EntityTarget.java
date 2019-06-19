package me.onebone.actaeon.target;

import cn.nukkit.math.Vector3;

/**
 * @author CreeperFace
 */
public class EntityTarget {

    private final Vector3 target;
    private final String identifier;

    private final float speed;

    private final boolean watchTarget;

    private final double maxDistance;

    private EntityTarget(Vector3 target, String identifier, float speed, boolean watchTarget, double maxDistance) {
        this.target = target;
        this.identifier = identifier;
        this.speed = speed;
        this.watchTarget = watchTarget;
        this.maxDistance = maxDistance;
    }

    public Vector3 getTarget() {
        return target;
    }

    public String getIdentifier() {
        return identifier;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isWatchTarget() {
        return watchTarget;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public static EntityTargetBuilder builder() {
        return new EntityTargetBuilder();
    }

    public static class EntityTargetBuilder {

        private Vector3 target;
        private String identifier;

        private float speed = 1;

        private boolean watchTarget = false;

        private double maxDistance = 2;

        EntityTargetBuilder(){

        }

        public EntityTargetBuilder target(Vector3 target) {
            this.target = target;
            return this;
        }

        public EntityTargetBuilder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public EntityTargetBuilder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public EntityTargetBuilder watchTarget(boolean watchTarget) {
            this.watchTarget = watchTarget;
            return this;
        }

        public EntityTargetBuilder maxDistance(double maxDistance) {
            this.maxDistance = maxDistance;
            return this;
        }

        public EntityTarget build() {
            return new EntityTarget(this.target, this.identifier, this.speed, this.watchTarget, this.maxDistance);
        }

        @Override
        public String toString() {
            return "EntityTarget.EntityTargetBuilder(target=" + this.target + ", identifier=" + this.identifier + ", speed=" + this.speed + ", watchTarget=" + this.watchTarget + ", maxDistance=" + this.maxDistance + ")";
        }
    }
}
