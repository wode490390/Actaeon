package me.onebone.actaeon.hook;


import cn.nukkit.Server;
import cn.nukkit.math.NukkitRandom;
import me.onebone.actaeon.Utils.Utils;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.task.ChickenEggTask;

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
public class ChickenEggHook extends MovingEntityHook {
    private int nextEggTick;

    public ChickenEggHook(MovingEntity animal) {
        super(animal);
        nextEggTick = Server.getInstance().getTick()+Utils.rand(6000,12000);
    }

    @Override
    public void onUpdate(int tick) {
        if(tick >= nextEggTick) {
            nextEggTick = tick+Utils.rand(6000,12000);
            this.entity.updateBotTask(new ChickenEggTask(entity));
        }
    }
}
