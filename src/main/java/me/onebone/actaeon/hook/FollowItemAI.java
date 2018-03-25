package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import me.onebone.actaeon.entity.MovingEntity;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author CreeperFace
 */
public class FollowItemAI extends MovingEntityHook {

    private Collection<Integer> items;
    private Player holder = null;
    private double range;

    private double delay;

    public FollowItemAI(MovingEntity entity, double range, Collection<Item> items) {
        super(entity);
        this.items = items.stream().map(Item::getId).collect(Collectors.toSet());
        this.range = range * range;
        setCompatibility(0b11);
    }

    @Override
    public boolean shouldExecute() {
        if (delay > 0) {
            delay--;
            return false;
        }

        this.holder = findClosestPlayer();
        return this.holder != null;
    }

    @Override
    public boolean canContinue() {
        return shouldExecute();
    }

    @Override
    public void onUpdate(int tick) {
        if (this.holder.distanceSquared(this.entity) < 6.25) {
            this.entity.getRoute().forceStop();
        } else {
            this.entity.setTarget(this.holder, "followItem", true);
        }
    }

    private Player findClosestPlayer() {
        Player near = null;
        double last = this.range;

        for (Player player : this.getEntity().getLevel().getPlayers().values()) {
            double distance = player.distanceSquared(getEntity());

            if (distance <= last) {
                Item item = player.getInventory().getItemInHand();

                if (items.contains(item.getId())) {
                    near = player;
                    last = distance;
                }
            }
        }

        return near;
    }

    @Override
    public void reset() {
        this.holder = null;
        this.entity.getRoute().forceStop();
        this.delay = 100;
    }
}