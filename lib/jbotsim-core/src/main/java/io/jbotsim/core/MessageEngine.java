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

package io.jbotsim.core;

/**
 * <p>The {@link MessageEngine} is responsible for the regular transmission of the available
 * {@link Message Messages} from any sender {@link Node} of the {@link Topology} to their destination.</p>
 *
 */
public interface MessageEngine {

    /**
     * <p>Sets the {@link Topology} to which the {@link MessageEngine} refers.</p>
     * @param topology a {@link Topology}.
     */
    void setTopology(Topology topology);

    /**
     * <p>Method responsible for the delivery of the {@link Message Messages}.
     * It is regularly called by the {@link Scheduler}.</p>
     */
    void onClock();

    /**
     * <p>Resets the {@link MessageEngine}.</p>
     */
    void reset();
}
