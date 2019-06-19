package me.onebone.actaeon.runnable;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import me.onebone.actaeon.route.RouteFinder;

/**
 * RouteFinderSearchAsyncTask
 * ===============
 * author: boybook
 * EaseCation Network Project
 * nukkit
 * ===============
 */
public class RouteFinderSearchAsyncTask extends AsyncTask {

    private final RouteFinder route;
    private int retryTimes = 0;
    private Level level;
    private Vector3 start;
    private Vector3 dest;
    private AxisAlignedBB bb;
    public Thread thread;

    /*public RouteFinderSearchAsyncTask(RouteFinder route) {
        this(route, null, null, null, null);
    }*/

    public long started;

    public RouteFinderSearchAsyncTask(RouteFinder route, Level level, Vector3 start, Vector3 dest, AxisAlignedBB bb) {
        this.route = route;
        this.level = level;
        this.start = start.clone();
        this.dest = dest.clone();
        this.bb = bb.clone();
    }

    @Override
    public void onRun() {
        started = System.currentTimeMillis();
        TaskWatchDog.tasks.add(this);
        thread = Thread.currentThread();

        while (this.retryTimes < 100) {
            if (!this.route.isSearching()) {
                if (this.level != null) {
                    this.route.setPositions(this.level, this.start, this.dest, this.bb);
                }
                this.route.search();
                //Server.getInstance().getLogger().notice("异步寻路线程-" + this.getTaskId() + " 开始寻路");
                return;
            } else {
                this.retryTimes++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
        Server.getInstance().getLogger().warning("异步寻路线程-" + this.getTaskId() + " 超过等待限制");
        this.route.forceStop();
    }
}
