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

/**
 * <p>The {@link LinkResolver} determines whether a {@link Node} can send a message to another one.</p>
 */
public class LinkResolver {
    /**
     * Determines whether a message sent from the first {@link Node} could be recieved by the second one.
     *
     * @param n1 the emitter {@link Node}
     * @param n2 the receiver {@link Node}
     * @return <code>true</code> if n2 can recieve a message from n1; <code>false</code> otherwise
     */
    public boolean isHeardBy(Node n1, Node n2) {
        return (n1.isWirelessEnabled() && n2.isWirelessEnabled()
                && n1.distance(n2) <= n1.getCommunicationRange());
    }
}
