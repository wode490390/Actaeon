package me.onebone.actaeon.entity;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.hook.AnimalGrowHook;
import me.onebone.actaeon.util.Utils;

/**
 * @author CreeperFace
 */
public abstract class EntityAgeable extends MovingEntity implements cn.nukkit.entity.EntityAgeable {

    public EntityAgeable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        if (isBaby()) {
            this.addHook("grow", new AnimalGrowHook(this, Utils.rand(20 * 60 * 10, 20 * 60 * 20)));
        }
    }

    @Override
    public boolean isBaby() {
        return false;
    }
}
