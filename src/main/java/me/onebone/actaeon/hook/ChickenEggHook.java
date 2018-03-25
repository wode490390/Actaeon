package me.onebone.actaeon.hook;


import cn.nukkit.Server;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.task.ChickenEggTask;
import me.onebone.actaeon.util.Utils;

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
        nextEggTick = Server.getInstance().getTick() + Utils.rand(6000, 12000);
    }

    @Override
    public boolean shouldExecute() {
        return Server.getInstance().getTick() <= nextEggTick;
    }

    @Override
    public void startExecuting() {
        this.entity.updateBotTask(new ChickenEggTask(entity));
    }

    @Override
    public boolean canContinue() {
        return false;
    }
}
