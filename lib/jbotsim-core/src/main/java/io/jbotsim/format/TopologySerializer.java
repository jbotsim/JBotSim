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
package io.jbotsim.format;

import io.jbotsim.core.Topology;

/**
 * Objects implementing {@link TopologySerializer} are able to (de)serialize a {@link Topology} into a specific
 * {@link String} representation.
 */
public interface TopologySerializer {
    /**
     * Returns a string representation of this topology. The output of this
     * method can be subsequently used to reconstruct a topology with the
     * {@link TopologySerializer#importTopology(Topology, String)} method. Only the nodes and wired links are exported
     * here (not the topology's properties).
     *
     * @param tp The {@link Topology} object which must be exported
     * @return the {@link String} representation of the {@link Topology}
     */
    String exportTopology(Topology tp);

    /**
     * Imports nodes and wired links from the specified string representation of a
     * topology.
     *
     * @param tp The {@link Topology} object which must be populated
     * @param s The {@link String} representation.
     */
    void importTopology(Topology tp, String s);
}
