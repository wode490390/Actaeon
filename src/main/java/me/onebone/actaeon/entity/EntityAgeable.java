package me.onebone.actaeon.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.Actaeon;

/**
 * @author CreeperFace
 */
public abstract class EntityAgeable extends MovingEntity implements cn.nukkit.entity.EntityAgeable {

    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;

    public EntityAgeable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.growingAge = namedTag.getInt("Age");
        this.forcedAge = namedTag.getInt("ForcedAge");
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = super.entityBaseTick(tickDiff);

        int growingAge = this.getGrowingAge();

        if (growingAge < 0) {
            ++growingAge;
            this.setGrowingAge(growingAge);

            if (growingAge == 0) {
                this.onGrowingAdult();
            }
        } else if (growingAge > 0) {
            --growingAge;
            this.setGrowingAge(growingAge);
        }

        return hasUpdate;
    }

    @Override
    public boolean isBaby() {
        return getDataFlag(DATA_FLAGS, DATA_FLAG_BABY);
    }

    public int getGrowingAge() {
        return this.growingAge;
    }

    public void ageUp(int amount, boolean forceAge) {
        int growingAge = this.getGrowingAge();
        int j = growingAge;
        growingAge = growingAge + amount * 20;

        if (growingAge > 0) {
            growingAge = 0;

            if (j < 0) {
                this.onGrowingAdult();
            }
        }

        int diff = growingAge - j;
        this.setGrowingAge(growingAge);

        if (forceAge) {
            this.forcedAge += diff;

            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }
    }

    public void addGrowth(int growth) {
        this.ageUp(growth, false);
    }

    public void setGrowingAge(int age) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, age < 0);
        this.growingAge = age;
        this.updateScale(isBaby());
    }

    protected void onGrowingAdult() {
    }

    public void updateScale(boolean baby) {
        this.setDataProperty(new FloatEntityData(DATA_SCALE, baby ? 0.5f : 1));
    }

    public EntityAgeable createBaby() {
        Entity baby = Actaeon.create(getNetworkId(), this);
        return baby instanceof EntityAgeable ? (EntityAgeable) baby : null;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("Age", getGrowingAge());
        this.namedTag.putInt("ForcedAge", forcedAge);
    }
}
