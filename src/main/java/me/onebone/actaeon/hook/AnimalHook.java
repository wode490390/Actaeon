package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.target.AreaPlayerHoldTargetFinder;
import me.onebone.actaeon.target.StrollingTargetFinder;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup All right reserved.
 * Welcome to DreamCity Server Address:dreamcity.top:19132
 * Created by WetABQ(Administrator) on 2018/2/11.
 * |||    ||    ||||                           ||        ||||||||     |||||||
 * |||   |||    |||               ||         ||  |      |||     ||   |||    |||
 * |||   |||    ||     ||||||  ||||||||     ||   ||      ||  ||||   |||      ||
 * ||  |||||   ||   |||   ||  ||||        ||| |||||     ||||||||   |        ||
 * ||  || ||  ||    ||  ||      |        |||||||| ||    ||     ||| ||      ||
 * ||||   ||||     ||    ||    ||  ||  |||       |||  ||||   |||   ||||||||
 * ||     |||      |||||||     |||||  |||       |||| ||||||||      |||||    |
 * ||||
 */
public class AnimalHook extends MovingEntityHook {

    private int radius;
    private long interval;
    private Item item;

    public AnimalHook(MovingEntity animal, long interval, Item item, int radius) {
        super(animal);
        this.radius = radius;
        this.interval = interval;
        this.item = item;
        this.radius = radius;
    }

    @Override
    public void onUpdate(int tick) {
        if (tick % 20 == 0) {
            Player near = null;
            double nearest = this.radius * this.radius;

            for (Player player : this.getEntity().getLevel().getPlayers().values()) {
                if (this.getEntity().distanceSquared(player) < nearest) {
                    near = player;
                    nearest = this.getEntity().distance(player);
                }
            }

            if (near != null) {
                if (entity.getTargetFinder() instanceof StrollingTargetFinder) {
                    entity.setTargetFinder(new AreaPlayerHoldTargetFinder(entity, interval, item, radius));
                }
            } else {
                if (entity.getTargetFinder() instanceof AreaPlayerHoldTargetFinder) {
                    entity.setTargetFinder(new StrollingTargetFinder(entity));
                }
            }
        }
    }
}
