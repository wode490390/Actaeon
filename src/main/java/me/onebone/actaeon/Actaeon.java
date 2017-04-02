package me.onebone.actaeon;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.entity.animal.Chicken;
import me.onebone.actaeon.entity.animal.Cow;
import me.onebone.actaeon.entity.animal.Pig;
import me.onebone.actaeon.entity.animal.Sheep;
import me.onebone.actaeon.entity.monster.Zombie;

public class Actaeon extends PluginBase{

	private static Actaeon instance;

	public static Actaeon getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		if (instance == null) instance = this;
	}

	public void onEnable(){
		this.saveDefaultConfig();

		this.registerEntity("Sheep", Sheep.class);
		this.registerEntity("Cow", Cow.class);
		this.registerEntity("Chicken", Chicken.class);
		this.registerEntity("Pig", Pig.class);
		this.registerEntity("Zombie", Zombie.class);

		Item.addCreativeItem(Item.get(Item.SPAWN_EGG, Zombie.NETWORK_ID));
	}

	private void registerEntity(String name, Class<? extends MovingEntity> clazz){
		Entity.registerEntity(name, clazz, true);
	}
}
