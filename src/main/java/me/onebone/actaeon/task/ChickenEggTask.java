package me.onebone.actaeon.task;

import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import me.onebone.actaeon.Utils.Utils;
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
public class ChickenEggTask extends MovingEntityTask {


    public ChickenEggTask(MovingEntity movingEntity){
        super(movingEntity);
    }

    @Override
    public void onUpdate(int i){
        entity.getLevel().dropItem(entity,Item.get(Item.EGG,0,1),new Vector3(0,0,0));
        this.entity.updateBotTask(null);
    }

    @Override
    public void forceStop() {

    }
}
