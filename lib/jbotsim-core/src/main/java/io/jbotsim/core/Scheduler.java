/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.core;

import io.jbotsim.core.event.ClockListener;

import java.util.List;

/**
 * <p>The {@link Scheduler}  defines JBotSim's scheduler.</p>
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
public class Scheduler {

    /**
     * Performs the regular scheduling operations.
     *
     * @param tp a {@link Topology} object on which the scheduling must take place
     * @param expiredListeners a list of {@link ClockListener} that are to be informed
     */
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
