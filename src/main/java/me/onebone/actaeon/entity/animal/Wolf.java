package me.onebone.actaeon.entity.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntityOwnable;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.Utils.Utils;
import me.onebone.actaeon.hook.AnimalGrowHook;
import me.onebone.actaeon.hook.AnimalHook;
import me.onebone.actaeon.hook.AttackHook;

public class Wolf extends Animal implements EntityAgeable, EntityOwnable{

	public static final int NETWORK_ID = 14;
	private boolean isBaby = false;
	
	private Player tamer = null;
	private boolean hasOwner = false;
	
	public Wolf(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
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
    
    @Override
    public boolean isBaby() {
    	return this.isBaby;
    }
    
    public Player getOwner() {
    	boolean ss = false;
    	if(this.tamer != null) {
    		ss = true;
    	}
		if(ss) {
			return this.tamer;
		} else {
			return null;
		}
    }
    
    public boolean hasOwner() {
    	return this.hasOwner;
    }
    
    public void setOwner(Player player) {
    	if(player != null && player.isOnline()) {
    		this.tamer = player;
    		this.hasOwner = true;
    	}
    }
    
    @Override
    public boolean onInteract(Player pla, Item item) {
    	if(!this.hasOwner()) {
    		if(item.getId() == Item.BONE) {
    			this.setOwner(pla);
    		}
    	}
    	return false;
    }
    
    @Override
    public boolean attack(EntityDamageEvent source) {
    	if(source instanceof EntityDamageByEntityEvent) {
    		Entity attacker = ((EntityDamageByEntityEvent)source).getDamager();
    		if(this.getOwner() != (Player) attacker || !this.hasOwner()) {
    			this.setHate(attacker);
    			this.addHook("attack", new AttackHook(this, 1, 2, 1000, 10, 180));
    		}
    	}
    	return false;
    }
    
    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(10);
        isBaby = Utils.rand(1, 10) == 1;
        setBaby(isBaby);
        if (isBaby) {
            this.addHook("grow", new AnimalGrowHook(this, Utils.rand(20 * 60 * 10, 20 * 60 * 20)));
        }
    }

	@Override
	public String getOwnerName() {
		return null;
	}

	@Override
	public void setOwnerName(String aname) {
		
		
	}
    
    

}
