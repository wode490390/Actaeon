package me.onebone.actaeon.target;

import cn.nukkit.math.Vector3;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.route.AdvancedRouteFinder;

import java.util.Random;

/**
 * StrollingTargetFinder
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public class StrollingTargetFinder extends TargetFinder {

    public int needFind = new Random().nextInt(3);
    public int needFindResetMax = 8;
    public int needFindResetUntil = 0;
    private double radius;
    public int findHighest = -1;  //<0为关闭寻找最高，0为至虚空，>0为向下方块格数限制

    public StrollingTargetFinder(MovingEntity entity) {
        this(entity, 5);
    }

    public StrollingTargetFinder(MovingEntity entity, double radius) {
        this(entity, radius, 1000 * (new Random().nextInt(2) + 2));
    }

    public StrollingTargetFinder(MovingEntity entity, double radius, long inter) {
        super(entity, inter);
        this.radius = radius;
    }

    @Override
    protected void find() {
        Random random = new Random();
        if (this.needFind > 0) {
            Vector3 base = this.entity.getRealTarget() != null ? this.entity.getTarget() : this.getEntity().getPosition();
            //for (int i = 0; i < 5; i++) {
            double r = random.nextDouble() * 360;
            double x = this.radius * Math.cos(Math.toRadians(r));
            double z = this.radius * Math.sin(Math.toRadians(r));
            double y = base.getY();
            if (this.findHighest >= 0 && this.getEntity().getRoute() instanceof AdvancedRouteFinder) {
                Vector3 highest = ((AdvancedRouteFinder)this.getEntity().getRoute()).getHighestUnder(x, y + 2, z, this.findHighest == 0 ? (int)y + 2 : this.findHighest);
                if (highest == null)
                    return;  //不可走, 重新尝试选点
                y = highest.getY() + 1;
            }
            this.entity.setTarget(new Vector3(base.getX() + x, y, base.getZ() + z), this.getEntity().getName());
            //break;
            //}
            this.needFind--;
            if (random.nextInt(10) < 2) {
                this.entity.setSprinting(true);
            } else {
                this.entity.setSprinting(false);
            }
        } else {
            this.entity.getRoute().forceStop();
            this.entity.setTarget(null, this.getEntity().getName());
        }
        if (this.needFind <= this.needFindResetUntil) this.needFind = this.needFindResetMax;
    }

}
