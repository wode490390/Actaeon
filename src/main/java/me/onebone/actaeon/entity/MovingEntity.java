package me.onebone.actaeon.entity;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import me.onebone.actaeon.hook.HookManager;
import me.onebone.actaeon.hook.MovingEntityHook;
import me.onebone.actaeon.route.AdvancedRouteFinder;
import me.onebone.actaeon.route.Node;
import me.onebone.actaeon.route.RouteFinder;
import me.onebone.actaeon.target.TargetFinder;
import me.onebone.actaeon.task.MovingEntityTask;

import java.util.Objects;

abstract public class MovingEntity extends EntityCreature {
    private boolean isKnockback = false;
    private RouteFinder route = null;
    private TargetFinder targetFinder = null;
    private Vector3 target = null;
    private Entity hate = null;
    private String targetSetter = "";
    public boolean routeLeading = true;
    private HookManager hookManager = new HookManager();
    private MovingEntityTask task = null;
    private boolean lookAtFront = true;

    public MovingEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.route = new AdvancedRouteFinder(this);
        //this.route = new SimpleRouteFinder(this);
        this.setImmobile(false);
    }

    public void setBaby(boolean isBaby) {
        this.setDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY, isBaby);
    }

    public void addHook(int priority, MovingEntityHook hook) {
        this.hookManager.addHook(priority, hook);
    }

    public void removeHook(MovingEntityHook hook) {
        this.hookManager.removeHook(hook);
    }

    @Override
    protected float getGravity() {
        return 0.092f;
    }

    public Entity getHate() {
        return hate;
    }

    public void setHate(Entity hate) {
        this.hate = hate;
    }

    public void jump() {
        if (this.onGround) {
            this.motionY = Math.sqrt(2 * getJumpHeight() * getGravity());
        }
    }

    @Override
    public boolean onUpdate(int currentTick) {
        super.onUpdate(currentTick);
        return true;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.closed) {
            return false;
        }

        this.hookManager.onUpdate(getServer().getTick());
        if (this.task != null) this.task.onUpdate(Server.getInstance().getTick());

        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (this.isKnockback) {                   // knockback 이 true 인 경우는 맞은 직후

        } else if (this.routeLeading && this.onGround) {
            this.motionX = this.motionZ = 0;
        }

        double friction = 1 - this.getDrag();

        if (this.onGround && (Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionZ) > 0.00001))
            friction *= this.getLevel().getBlock(this.temporalVector.setComponents((int) Math.floor(this.x), (int) Math.floor(this.y - 1), (int) Math.floor(this.z) - 1)).getFrictionFactor();

        this.motionX *= friction;
        this.motionZ *= friction;

        if (this.targetFinder != null) this.targetFinder.onUpdate();

        if (this.route.isSearching() && System.currentTimeMillis() - this.route.stopRouteFindUntil > 1000) {
            this.route.forceStop();
        }

        double swim = 0;

        for (Block block : getCollisionBlocks()) {
            if (block instanceof BlockLiquid) {
                float f = ((BlockLiquid) block).getFluidHeightPercent();
                double minY = block.getFloorY();
                double maxY = minY + 1 - f;

                swim = Math.max(swim, this.boundingBox.maxY >= maxY ? maxY - this.boundingBox.minY : this.boundingBox.maxY - minY);
                break;
            }
        }

        if (this.routeLeading && (this.onGround || swim > 0) && this.hasSetTarget() && !this.route.isSearching() && System.currentTimeMillis() >= this.route.stopRouteFindUntil && (this.route.getDestination() == null || this.route.getDestination().distance(this.getTarget()) > 2)) { // 대상이 이동함
            //Server.getInstance().getScheduler().scheduleAsyncTask(new RouteFinderSearchAsyncTask(this.route, this.level, this, this.getTarget(), this.boundingBox));
            this.route.setPositions(this.level, this.clone(), getTarget().clone(), this.boundingBox.clone());
            this.route.search();

			/*if(this.route.isSearching()) this.route.research();
            else this.route.search();*/

            hasUpdate = true;
        }

        if (!this.isImmobile()) {
            if (this.routeLeading && !this.isKnockback && !this.route.isSearching() && this.route.isSuccess() && this.route.hasRoute()) { // entity has route to go
                hasUpdate = true;
                //MainLogger.getLogger().info("MOVE");

                Node node = this.route.get();
                if (node != null) {
                    //level.addParticle(new cn.nukkit.level.particle.RedstoneParticle(node.getVector3(), 2));
                    Vector3 vec = node.getVector3();
                    double diffX = Math.pow(vec.x - this.x, 2);
                    double diffZ = Math.pow(vec.z - this.z, 2);

                    if (diffX + diffZ == 0) {
                        if (!this.route.next()) {
                            this.route.arrived();
                            //Server.getInstance().getLogger().warning(vec.toString());
                        }
                    } else {
                        int negX = vec.x - this.x < 0 ? -1 : 1;
                        int negZ = vec.z - this.z < 0 ? -1 : 1;

                        this.motionX = Math.min(Math.abs(vec.x - this.x), diffX / (diffX + diffZ) * this.getMovementSpeed()) * negX;
                        this.motionZ = Math.min(Math.abs(vec.z - this.z), diffZ / (diffX + diffZ) * this.getMovementSpeed()) * negZ;
                        if (this.lookAtFront) {
                            double angle = Math.atan2(vec.z - this.z, vec.x - this.x);
                            this.setRotation((angle * 180) / Math.PI - 90, 0);
                        }

                        /*if(getRealTarget() instanceof Entity)
                            lookAt(getTarget());*/
                    }
                }
            }

            for (Entity entity : this.getLevel().getCollidingEntities(this.boundingBox)) {
                if (this.canCollide() && this.canCollideWith(entity)) {
                    /*Vector3 motion = this.subtract(entity);
                    this.motionX += motion.x / 2;
                    this.motionZ += motion.z / 2;*/
                    applyEntityCollision(entity);
                }
            }

            if (swim != 0) {
                if (this.level.rand.nextFloat() < 0.8) {
                    this.motionY = Math.min(0.2, this.motionY + (0.2 * swim));
                }

                this.motionX *= 0.3;
                this.motionZ *= 0.3;
            }

            if ((this.motionX != 0 || this.motionZ != 0) && this.isCollidedHorizontally) {
                AxisAlignedBB bb = this.boundingBox.clone().offset(this.motionX, this.motionY, this.motionZ);
                Block[] blocks = this.level.getCollisionBlocks(bb);

                boolean jump = false;
                boolean step = true;

                for (Block b : blocks) {
                    AxisAlignedBB blockBB = b.getBoundingBox();
                    if (blockBB == null || b.canPassThrough())
                        continue;

                    double diffY = blockBB.maxY - this.boundingBox.minY;
                    if (diffY < getJumpHeight()) {
                        jump = true;
                    }

                    if (step)
                        step = diffY <= getStepHeight();
                }

                if (jump && !step) {
                    this.jump();
                }
            }

            this.move(this.motionX, this.motionY, this.motionZ);

            this.checkGround();
            if (!this.onGround && swim == 0) {
                this.motionY -= this.getGravity();
                //Server.getInstance().getLogger().warning(this.getId() + ": 不在地面, 掉落 motionY=" + this.motionY);
                hasUpdate = true;
            } else {
                this.isKnockback = false;
            }
        }


        return hasUpdate;
    }

    public double getRange() {
        return 100.0;
    }

    public void setTarget(Vector3 vec, String identifier) {
        this.setTarget(vec, identifier, false);
    }

    public void setTarget(Vector3 vec, String identifier, boolean forceSearch) {
        if (identifier == null) return;
        if (Objects.equals(this.target, vec))
            return;

        if (forceSearch || !this.hasSetTarget() || identifier.equals(this.targetSetter)) {
            this.target = vec;

            this.targetSetter = identifier;
        }

        if (this.hasSetTarget() && (forceSearch || !this.route.hasRoute())) {
            this.route.forceStop();
            this.route.setPositions(this.level, this.clone(), getTarget().clone(), this.boundingBox.clone());
            this.route.search();
			/*if(this.route.isSearching()) this.route.research();
			else this.route.search();*/
        }
    }

    public Vector3 getRealTarget() {
        return this.target;
    }

    public Vector3 getTarget() {
        return new Vector3(this.target.x, this.target.y, this.target.z);
    }

    /**
     * Returns whether the entity has following target
     * Entity will try to move to position where target exists
     */
    public boolean hasFollowingTarget() {
        return this.route.getDestination() != null && this.target != null && this.distanceSquared(this.target) < this.getRange();
    }

    /**
     * Returns whether the entity has set its target
     * The entity may not follow the target if there is following target and set target is different
     * If following distance of target is too far to follow or cannot reach, set target will be the next following target
     */
    public boolean hasSetTarget() {
        return this.target != null && this.distanceSquared(this.target) < this.getRange();
    }

    @Override
    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy, double dz) {
        this.isCollidedVertically = movY != dy;
        this.isCollidedHorizontally = (movX != dx || movZ != dz);
        this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);

        // this.onGround = (movY != dy && movY < 0);
        // onGround 는 onUpdate 에서 확인
    }

    private void checkGround() {
        AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.level.getTickRate() > 1 ? this.boundingBox.getOffsetBoundingBox(0, -1, 0) : this.boundingBox.addCoord(0, -1, 0), false);

        double maxY = 0;
        for (AxisAlignedBB bb : list) {
            if (bb.maxY > maxY) {
                maxY = bb.maxY;
            }
        }

        this.onGround = (maxY == this.boundingBox.minY);
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        UpdateAttributesPacket pk0 = new UpdateAttributesPacket();
        pk0.entityId = this.getId();
        pk0.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(this.getMaxHealth()).setValue(this.getHealth()),
        };
        this.getLevel().addChunkPacket(this.chunk.getX(), this.chunk.getZ(), pk0);
    }

    @Override
    public void setMaxHealth(int maxHealth) {
        super.setMaxHealth(maxHealth);
        if (this.getHealth() > maxHealth) this.health = maxHealth;
        UpdateAttributesPacket pk0 = new UpdateAttributesPacket();
        pk0.entityId = this.getId();
        pk0.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(this.getMaxHealth()).setValue(this.getHealth()),
        };
        this.getLevel().addChunkPacket(this.chunk.getX(), this.chunk.getZ(), pk0);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI);
    }

    @Override
    public void knockBack(Entity attacker, double damage, double x, double z, double base) {
        this.isKnockback = true;

        super.knockBack(attacker, damage, x, z, base / 2);
    }

    public void setRoute(RouteFinder route) {
        this.route = route;
    }

    public RouteFinder getRoute() {
        return route;
    }

    public TargetFinder getTargetFinder() {
        return targetFinder;
    }

    public void setTargetFinder(TargetFinder targetFinder) {
        this.targetFinder = targetFinder;
    }

    public void updateBotTask(MovingEntityTask task) {
        if (this.task != null) this.task.forceStop();
        this.task = task;
        if (task != null) this.task.onUpdate(Server.getInstance().getTick());
    }

    public MovingEntityTask getTask() {
        return task;
    }

    public boolean isLookAtFront() {
        return lookAtFront;
    }

    public void setLookAtFront(boolean lookAtFront) {
        this.lookAtFront = lookAtFront;
    }

    public void lookAt(Vector3 pos) {
        Vector3 diff = pos.subtract(this.add(0, getEyeHeight()));

        double length = Math.sqrt(diff.x * diff.x + diff.z * diff.z);

        double newPitch = Math.atan2(diff.z, diff.x) * (180 / Math.PI) - 90;
        double newYaw = -(Math.atan2(diff.y, length) * (180 / Math.PI));

        this.setRotation(newYaw, newPitch);
    }

    public double getJumpHeight() {
        return 1.25;
    }

    /*public boolean isBaby() {
        return false;
    }*/

    public int getAge() {
        return age;
    }
}

