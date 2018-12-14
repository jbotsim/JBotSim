package io.jbotsim.core;

import io.jbotsim.core.event.ClockListener;

import java.util.List;

/**
 * <p>The {@link DefaultScheduler} is the default implementation of the {@link Scheduler} used by the {@link Topology} to
 * perform actions on the clock.</p>
 *
 * <p>On each clock, it performs the following:</p>
 * <ol>
 *     <li>performs messages processing (via {@link MessageEngine#onClock()}</li>
 *     <li>performs onPreClock on each {@link Node} (via {@link Node#onPreClock()}</li>
 *     <li>performs onPreClock on each {@link Node} (via {@link Node#onPreClock()}</li>
 *     <li>performs onClock on each {@link Node} (via {@link Node#onClock()}</li>
 *     <li>performs onPostClock on each {@link Node} (via {@link Node#onPostClock()}</li>
 *     <li>performs onClock on the {@link Topology} (via {@link Topology#onClock()}</li>
 *     <li>performs remaining listeners work (via {@link ClockListener#onClock()}</li>
 * </ol>
 */
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
