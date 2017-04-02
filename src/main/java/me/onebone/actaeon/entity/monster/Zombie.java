package me.onebone.actaeon.entity.monster;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import me.onebone.actaeon.entity.Climbable;
import me.onebone.actaeon.entity.Fallable;
import me.onebone.actaeon.entity.animal.Animal;
import me.onebone.actaeon.target.AreaHaterTargetFinder;
import me.onebone.actaeon.target.AreaPlayerHoldTargetFinder;

public class Zombie extends Monster implements EntityAgeable, Fallable, Climbable{
	public static final int NETWORK_ID = 32;

	public Zombie(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
		this.setTargetFinder(new AreaHaterTargetFinder(this, 500, 20000));
	}

	@Override
	public float getWidth(){
		return 0.6f;
	}

	@Override
	public float getLength() {
		return 0.6f;
	}

	@Override
	protected float getGravity() {
		return 0.05f;
	}

	@Override
	public float getHeight(){
		if (isBaby()){
			return 0.8f;
		}
		return 1.8f;
	}

	@Override
	public float getEyeHeight(){
		if (isBaby()){
			return 0.51f;
		}
		return 0.7f;
	}

	@Override
	public Item[] getDrops(){
		return new Item[]{Item.get(Item.ROTTEN_FLESH)};
	}

	@Override
	public boolean entityBaseTick(int tickDiff){
        if (System.currentTimeMillis() > this.attackCoolUntil && this.hasSetTarget() && this.getRealTarget() instanceof Player) {
            Player player = (Player) this.getRealTarget();
            if (player != null && this.distance(player) <= 1) {
                player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageByEntityEvent.CAUSE_ENTITY_ATTACK, 2));
                AnimatePacket pk = new AnimatePacket();
                pk.eid = this.getId();
                pk.action = 3;
                for (Player p: this.getLevel().getPlayers().values()) {
                    p.dataPacket(pk);
                }
                this.attackCoolUntil = System.currentTimeMillis() + 1000;
            }
        }
		return super.entityBaseTick(tickDiff);
	}

	@Override
	public int getNetworkId(){
		return NETWORK_ID;
	}

	@Override
	protected void initEntity(){
		super.initEntity();
		setMaxHealth(20);
	}

	@Override
	public boolean isBaby(){
		return false;
	}
}
