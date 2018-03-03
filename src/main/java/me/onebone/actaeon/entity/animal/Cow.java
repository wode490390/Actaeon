package me.onebone.actaeon.entity.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.Utils.Utils;
import me.onebone.actaeon.hook.AnimalGrowHook;
import me.onebone.actaeon.hook.AnimalHook;

import java.util.Random;

public class Cow extends Animal implements EntityAgeable{
	public static final int NETWORK_ID = 11;
	private boolean isBaby = false;

	public Cow(FullChunk chunk, CompoundTag nbt){
		super(chunk, nbt);
		this.addHook("targetFinder", new AnimalHook(this, 500, Item.get(Item.WHEAT), 10));
	}

	@Override
	public int getNetworkId(){
		return NETWORK_ID;
	}

	@Override
	public float getWidth(){
		return 0.9f;
	}

	@Override
	public float getHeight(){
		if (isBaby()) {
			return 0.65f;
		}
		return 1.3f;
	}

	@Override
	public float getEyeHeight(){
		if (isBaby()){
			return 0.65f;
		}
		return 1.2f;
	}

	@Override
	public boolean isBaby(){
		return isBaby;
	}

	@Override
	public Item[] getDrops(){
		if(!isBaby()) {
			Random random = new Random();
			Item leather = Item.get(Item.LEATHER, 0, random.nextInt(2));
			Item meat = Item.get(Item.RAW_BEEF, 0, random.nextInt(3) + 1);
			EntityDamageEvent cause = this.getLastDamageCause();
			if (cause.getCause() == EntityDamageEvent.DamageCause.FIRE) {
				meat = Item.get(Item.STEAK, 0, random.nextInt(3) + 1);
			}
			this.getLevel().dropExpOrb(this,random.nextInt(3) + 1);
			return new Item[]{leather, meat};
		}else{
			return new Item[0];
		}
	}

	@Override
	public boolean entityBaseTick(int tickDiff){

		return super.entityBaseTick(tickDiff);
	}

	@Override
	public boolean onInteract(Player player, Item item) {
		if(item.getId() == Item.BUCKET) {
			player.getInventory().addItem(Item.get(335,0,1));
			return true;
		}
		return false;
	}

	@Override
	protected void initEntity(){
		super.initEntity();
		setMaxHealth(10);
		isBaby = Utils.rand(1,10) == 1;
		setBaby(isBaby);
		if(isBaby){
			this.addHook("grow", new AnimalGrowHook(this, Utils.rand(20*60*10,20*60*20)));
		}
	}

}
