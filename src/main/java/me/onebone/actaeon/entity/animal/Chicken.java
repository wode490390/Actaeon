package me.onebone.actaeon.entity.animal;

import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.Utils.Utils;
import me.onebone.actaeon.entity.Fallable;
import me.onebone.actaeon.hook.AnimalGrowHook;
import me.onebone.actaeon.hook.AnimalHook;
import me.onebone.actaeon.hook.ChickenEggHook;

public class Chicken extends Animal implements EntityAgeable, Fallable {
    public static final int NETWORK_ID = 10;
    private boolean isBaby = false;

    public Chicken(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.addHook("targetFinder", new AnimalHook(this, 500, Item.get(Item.WHEAT_SEEDS), 10));
        this.addHook("egg", new ChickenEggHook(this));
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public Item[] getDrops() {
        if (!isBaby()) {
            Item chicken;
            if (this.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                chicken = Item.get(Item.COOKED_CHICKEN);
            } else {
                chicken = Item.get(Item.RAW_CHICKEN);
            }
            this.getLevel().dropExpOrb(this, Utils.rand(1, 4));
            return new Item[]{chicken, Item.get(Item.FEATHER, 0, Utils.rand(0, 3))};
        } else {
            return new Item[0];
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(4);
        isBaby = (Utils.rand(1, 11) == 1);
        setBaby(isBaby);
        if (isBaby) {
            this.addHook("grow", new AnimalGrowHook(this, Utils.rand(20 * 60 * 10, 20 * 60 * 20)));
        }
    }

    @Override
    public boolean isBaby() {
        return isBaby;
    }

}
