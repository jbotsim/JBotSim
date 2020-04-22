/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

package io.jbotsim.core.event;

import io.jbotsim.core.Topology;

public abstract class FilteredClockListener implements ClockListener {
    private Topology topology;
    private ClockListener listener;

    public FilteredClockListener(Topology tp, ClockListener listener) {
        this.topology = tp;
        this.listener = listener;
    }

    @Override
    public void onClock() {
        if (shouldTriggerListener(topology, topology.getTime()))
            listener.onClock();
    }

    protected abstract boolean shouldTriggerListener(Topology tp, int currentTime);
}
