package me.onebone.actaeon.hook;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import me.onebone.actaeon.Actaeon;
import me.onebone.actaeon.entity.EntityAgeable;
import me.onebone.actaeon.entity.animal.Animal;

import java.util.Random;

/**
 * @author CreeperFace
 */
public class AnimalMateHook extends MovingEntityHook {

    private Animal animal;
    private Animal target;

    private int spawnBabyDelay;

    public AnimalMateHook(Animal entity) {
        super(entity);
        this.animal = entity;
        this.setCompatibility(0b11);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.animal.isInLove())
            return false;

        this.target = getNearby();
        return target != null;
    }

    @Override
    public boolean canContinue() {
        return target.isAlive() && target.isInLove() && this.spawnBabyDelay < 60;
    }

    @Override
    public void reset() {
        this.target = null;
        this.spawnBabyDelay = 60;
    }

    @Override
    public void onUpdate(int tick) {
        this.animal.setTarget(this.target, target.getName(), true);

        if (spawnBabyDelay++ >= 60 && animal.distanceSquared(target) < 9) {
            spawnBaby();
        }
    }

    private Animal getNearby() {
        AxisAlignedBB bb = animal.getBoundingBox().grow(8, 8, 8);
        double dist = Double.MAX_VALUE;
        Animal target = null;

        for (Entity entity : animal.getLevel().getNearbyEntities(bb, animal)) {
            double l;
            if (entity.getClass() == animal.getClass() && ((Animal) entity).isInLove() && (l = animal.distanceSquared(entity)) < dist) {
                target = (Animal) entity;
                dist = l;
            }
        }

        return target;
    }

    private void spawnBaby() {
        EntityAgeable baby = this.animal.createBaby();
        if (baby == null) {
            return;
        }

        //TODO: achievement for tamer

        this.animal.setGrowingAge(6000);
        this.animal.resetInLove();

        this.target.setGrowingAge(6000);
        this.target.resetInLove();

        baby.setGrowingAge(-24000);
        baby.spawnToAll();

        Random random = this.animal.getLevel().rand;

        for (int i = 0; i < 7; ++i) {
            double offsetX = random.nextDouble() * this.animal.getWidth() * 2 - this.animal.getWidth();
            double offsetY = 0.5 + random.nextDouble() * this.animal.getHeight();
            double offsetZ = random.nextDouble() * this.animal.getWidth() * 2 - this.animal.getWidth();
            this.animal.getLevel().addParticle(new HeartParticle(new Vector3(this.animal.getX() + offsetX, this.animal.getY() + offsetY, this.animal.getZ() + offsetZ)));
        }

        if (this.animal.getLevel().getGameRules().getBoolean("doMobLoot")) {
            EntityXPOrb orb = (EntityXPOrb) Actaeon.create(EntityXPOrb.NETWORK_ID, animal);
            orb.setExp(random.nextInt(7) + 1);

            orb.spawnToAll();
        }
    }
}
