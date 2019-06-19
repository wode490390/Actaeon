package me.onebone.actaeon;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.entity.animal.*;
import me.onebone.actaeon.entity.monster.Zombie;
import me.onebone.actaeon.runnable.TaskWatchDog;
import me.onebone.actaeon.task.SpawnTask;

public class Actaeon extends PluginBase {

    private static Actaeon instance;

    public static Actaeon getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        if (instance == null) {
            instance = this;
        }
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        new TaskWatchDog().start();

        this.registerEntity("Sheep", Sheep.class);
        this.registerEntity("Cow", Cow.class);
        this.registerEntity("Chicken", Chicken.class);
        this.registerEntity("Pig", Pig.class);
        this.registerEntity("Zombie", Zombie.class);
        this.registerEntity("Wolf", Wolf.class);

        int tick = getConfig().getInt("spawn-tick");

        if (tick > 0) {
            this.getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SpawnTask(this), tick, tick);
        }
    }

    private void registerEntity(String name, Class<? extends MovingEntity> clazz) {
        Entity.registerEntity(name, clazz, true);
    }

    public static Entity create(Object type, Position source, Object... args) {
        return Entity.createEntity(type.toString(), source, args);
    }
}
