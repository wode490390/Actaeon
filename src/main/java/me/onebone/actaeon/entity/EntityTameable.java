package me.onebone.actaeon.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityOwnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.entity.animal.Animal;

/**
 * @author CreeperFace
 */
public abstract class EntityTameable extends Animal implements EntityOwnable {

    protected String owner;
    protected Player ownerInstance;

    public EntityTameable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        //TODO: load owner from NBT
    }

    public boolean isTamed() {
        return getDataFlag(DATA_FLAGS, DATA_FLAG_TAMED);
    }

    public void setTamed(boolean tamed) {
        setDataFlag(DATA_FLAGS, DATA_FLAG_TAMED, tamed);
    }

    public boolean isSitting() {
        return getDataFlag(DATA_FLAGS, DATA_FLAG_SITTING);
    }

    public void setSitting(boolean sitting) {
        setDataFlag(DATA_FLAGS, DATA_FLAG_SITTING, sitting);
    }

    @Override
    public Player getOwner() {
        if (ownerInstance == null || ownerInstance.closed) {
            ownerInstance = this.getServer().getPlayerExact(this.owner);
        }

        return ownerInstance;
    }

    @Override
    public String getOwnerName() {
        return owner;
    }

    @Override
    public void setOwnerName(String owner) {
        this.owner = owner;
    }

    public void setOwner(Player p) {
        this.ownerInstance = p;
        this.owner = p.getName();
    }

    public boolean isOwner(Player player) {
        return owner.equalsIgnoreCase(player.getName());
    }

    public boolean hasOwner() {
        return owner != null;
    }
}
