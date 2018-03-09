package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import me.onebone.actaeon.entity.MovingEntity;

/**
 * @author CreeperFace
 */
public class FollowItemAI extends MovingEntityHook {

    private Item item;
    private Player holder = null;
    private double range;

    public FollowItemAI(MovingEntity entity, Item item, double range) {
        super(entity);
        this.item = item;
        this.range = range * range;
    }

    @Override
    public void onUpdate(int tick) {
        if (tick % 10 == 0) {
            if (holder != null) {
                if (holder.closed) {
                    holder = null;
                    return;
                }

                Item item = holder.getInventory().getItemInHand();
                if (item.getId() != this.item.getId() || item.getDamage() != this.item.getDamage()) {
                    this.holder = null;
                    this.getEntity().setTarget(null, this.getEntity().getName());
                } else {
                    return;
                }
            }

            Player near = null;
            double last = this.range;

            for (Player player : this.getEntity().getLevel().getPlayers().values()) {
                double distance = player.distanceSquared(getEntity());

                if (distance <= last) {
                    Item item = player.getInventory().getItemInHand();

                    if (item.getId() == this.item.getId() && item.getDamage() == this.item.getDamage()) {
                        near = player;
                        last = distance;
                    }
                }
            }

            if (near != null) {
                this.holder = near;
                this.getEntity().getRoute().forceStop();
                //this.getEntity().get

                this.getEntity().setTarget(near, "itemfollow", true);
            }
        }
    }
}
