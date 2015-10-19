package jbotsim;

import jbotsim.Topology;
import jbotsim.event.ClockListener;

/**
 * Created by acasteig on 10/19/15.
 */
public interface NodeScheduler {
    void onClock(Topology tp);
}
