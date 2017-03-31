package me.onebone.actaeon.task;

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

    private RouteFinder route;

    public RouteFinderSearchAsyncTask(RouteFinder route) {
        this.route = route;
    }

    @Override
    public void onRun() {
        if(!this.route.isSearching()) this.route.search();
    }
}
