package me.onebone.actaeon.entity.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.entity.EntityTameable;
import me.onebone.actaeon.hook.AnimalHook;
import me.onebone.actaeon.hook.AttackHook;

public class Wolf extends EntityTameable {

	public static final int NETWORK_ID = 14;
	
	public Wolf(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);

		setMaxHealth(isTamed() ? 20 : 8);
		this.addHook("targetFinder", new AnimalHook(this, 500, Item.get(Item.BONE), 10));
	}

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}
	
    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 0.85f;
    }

	public boolean isAngry() {
		return getDataFlag(DATA_FLAGS, DATA_FLAG_ANGRY);
	}

	public void setAngry(boolean angry) {
		setDataFlag(DATA_FLAGS, DATA_FLAG_ANGRY, angry);
	}
    
    @Override
    public boolean onInteract(Player pla, Item item) {
    	if(!this.hasOwner()) {
			if (item.getId() == Item.BONE && !isAngry()) {
				item.count--;

				if (this.level.rand.nextInt(3) == 0) {
					setTamed(true);
					setMaxHealth(20);
					setHealth(20);
					setSitting(true);
				} else {
					//TODO: effect
				}

    			this.setOwner(pla);
    		}
    	}
    	return false;
    }
    
    @Override
    public boolean attack(EntityDamageEvent source) {
    	if(source instanceof EntityDamageByEntityEvent) {
    		Entity attacker = ((EntityDamageByEntityEvent)source).getDamager();
			if (!this.getOwnerName().equalsIgnoreCase(attacker.getName()) || !this.hasOwner()) {
    			this.setHate(attacker);
				this.addHook("attack", new AttackHook(this, 1, 4, 1000, 10, 180));
    		}
    	}
    	return false;
    }
}
