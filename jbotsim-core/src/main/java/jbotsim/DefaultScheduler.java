package jbotsim;

import jbotsim.event.ClockListener;

import java.util.List;

public class DefaultScheduler implements Scheduler {

    @Override
    public void onClock(Topology tp, List<ClockListener> expiredListeners) {
        // Delivers messages first
        tp.getMessageEngine().onClock();
        // Then give the hand to the nodes
        for (Node node : tp.getNodes())
            node.onPreClock();
        for (Node node : tp.getNodes())
            node.onClock();
        for (Node node : tp.getNodes())
            node.onPostClock();
        // Then to the topology itself
        tp.onClock();
        // And finally the other listeners
        for (ClockListener cl : expiredListeners)
            cl.onClock();
    }
}
