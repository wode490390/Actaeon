package me.onebone.actaeon.hook;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockTallGrass;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.network.protocol.EntityEventPacket;
import me.onebone.actaeon.entity.animal.Animal;

/**
 * Created by CreeperFace on 15.7.2017.
 */
public class EatGrassHook extends MovingEntityHook {

    private int timer = 40;
    private Animal animal;

    public EatGrassHook(Animal entity) {
        super(entity);
        this.animal = entity;
        setCompatibility(0b111);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.animal.onGround || this.animal.level.rand.nextInt(this.animal.isBaby() ? 50 : 100) != 0) {
            return false;
        } else {
            Block block = this.entity.getLevelBlock();

            return block instanceof BlockTallGrass || block.down() instanceof BlockGrass;
        }
    }

    @Override
    public void startExecuting() {
        this.timer = 40;

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = this.getEntity().getId();
        pk.event = EntityEventPacket.EAT_GRASS_ANIMATION;
        Server.broadcastPacket(this.getEntity().getViewers().values(), pk);

        this.entity.getRoute().forceStop();
        this.entity.routeLeading = false;
    }

    @Override
    public void reset() {
        timer = 0;
        this.entity.routeLeading = true;
    }

    @Override
    public boolean canContinue() {
        return timer > 0;
    }

    @Override
    public void onUpdate(int tick) {
        this.timer = Math.max(0, this.timer - 1);

        if (this.timer == 4) {
            Block block = this.entity.getLevelBlock();

            if (block instanceof BlockTallGrass) {
                if (this.entity.level.getGameRules().getBoolean("mobGriefing")) {
                    this.entity.level.useBreakOn(block);
                }

                this.animal.eatGrass();
            } else {
                block = block.down();

                if (block.getId() == Block.GRASS) {
                    if (this.entity.level.getGameRules().getBoolean("mobGriefing")) {
                        this.entity.level.addParticle(new DestroyBlockParticle(block, block));
                        this.entity.level.setBlock(block, new BlockDirt(), true, false);
                    }

                    this.animal.eatGrass();
                }
            }
        }
    }
}
