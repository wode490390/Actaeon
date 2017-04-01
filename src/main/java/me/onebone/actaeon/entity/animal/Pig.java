package me.onebone.actaeon.entity.animal;

import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.target.AreaPlayerHoldTargetFinder;

public class Pig extends Animal implements EntityAgeable{
	public static final int NETWORK_ID = 12;

	public Pig(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
		this.setTargetFinder(new AreaPlayerHoldTargetFinder(this, 500, Item.get(Item.WHEAT), 100));
	}

	@Override
	public float getWidth() {
		return 0.9f;
	}

	@Override
	public float getHeight() {
		if (isBaby()) {
			return 0.9f; // No have information
		}
		return 0.9f;
	}

	@Override
	public float getEyeHeight() {
		if (isBaby()) {
			return 0.9f; // No have information
		}
		return 0.9f;
	}

	@Override
	public boolean entityBaseTick(int tickDiff){
		return super.entityBaseTick(tickDiff);
	}

	@Override
	public String getName() {
		return this.getNameTag();
	}

	@Override
	public Item[] getDrops() {
		return new Item[]{Item.get(Item.RAW_PORKCHOP)};
	}

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override
	protected void initEntity() {
		super.initEntity();
		setMaxHealth(10);
	}

	@Override
	public boolean isBaby(){
		return false;
	}
}
