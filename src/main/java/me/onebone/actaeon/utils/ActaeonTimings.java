package me.onebone.actaeon.Utils;

import co.aikar.timings.Timing;
import co.aikar.timings.TimingsManager;

/**
 * @author CreeperFace
 */
public class ActaeonTimings {

    public static final Timing routeFindTiming;

    static {
        routeFindTiming = TimingsManager.getTiming("RouteFind");
    }
}
