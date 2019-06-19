package me.onebone.actaeon.entity.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import com.google.common.collect.Sets;
import me.onebone.actaeon.hook.*;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Cow extends Animal implements EntityAgeable {

    public static final int NETWORK_ID = 11;

    private static final Set<Item> FOLLOW_ITEMS = Sets.newHashSet(
            Item.get(Item.WHEAT));

    public Cow(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.addHook(1, new AnimalMateHook(this));
        this.addHook(2, new FollowItemAI(this, 10, FOLLOW_ITEMS));
        this.addHook(3, new FollowParentHook(this));
        this.addHook(4, new WanderHook(this));
        this.addHook(5, new WatchClosestHook(this, Player.class, 6));
        this.addHook(6, new LookIdleHook(this));

        setMaxHealth(10);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.2f;
    }

    @Override
    public Item[] getDrops() {
        if (!isBaby()) {
            Item leather = Item.get(Item.LEATHER, 0, ThreadLocalRandom.current().nextInt(2));
            Item meat = Item.get(Item.RAW_BEEF, 0, ThreadLocalRandom.current().nextInt(3) + 1);
            EntityDamageEvent cause = this.getLastDamageCause();
            if (cause.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                meat = Item.get(Item.STEAK, 0, ThreadLocalRandom.current().nextInt(3) + 1);
            }
            this.getLevel().dropExpOrb(this, ThreadLocalRandom.current().nextInt(3) + 1);
            return new Item[]{leather, meat};
        }
        return new Item[0];
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.getId() == Item.BUCKET) {
            player.getInventory().addItem(Item.get(335, 0, 1));
            return true;
        }

        return super.onInteract(player, item);
    }
}
