package me.onebone.actaeon.hook;

import cn.nukkit.Server;
import me.onebone.actaeon.entity.MovingEntity;

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
public class AnimalGrowHook extends MovingEntityHook {
    private int growTick;

    public AnimalGrowHook(MovingEntity animal, int growTick) {
        super(animal);
        this.growTick = Server.getInstance().getTick() + growTick;
    }

    @Override
    public void onUpdate(int tick) {
        if (tick >= growTick) {
            entity.setBaby(false);
            entity.removeHook("grow");
        }
    }
}
