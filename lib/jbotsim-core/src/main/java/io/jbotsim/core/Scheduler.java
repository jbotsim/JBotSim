/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package io.jbotsim.core;

import io.jbotsim.core.event.ClockListener;

import java.util.List;

/**
 * <p>The {@link Scheduler} defines JBotSim's scheduler.</p>
 *
 * <p>It provides an {@link #onClock(Topology, List)} method which is supposed to be called regularly.</p>
 */
public interface Scheduler {

    /**
     * Performs the regular scheduling operations.
     *
     * @param tp a {@link Topology} object on which the scheduling must take place
     * @param expiredListeners a list of {@link ClockListener} that are to be informed
     */
    void onClock(Topology tp, List<ClockListener> expiredListeners);
}
