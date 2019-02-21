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
package io.jbotsim.topology.generators;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

/**
 * <p>The {@link TorusGenerator} is a {@link TopologyGenerator} used to generated tori of {@link Node}s.</p>
 *
 * <p>This implementation relies on the {@link GridGenerator}.</p>
 */
public class TorusGenerator extends GridGenerator {

    /**
     * Creates a {@link TorusGenerator} creating a torus with nbRows rows and nbColumns columns.

     * @param nbRows the amount of desired rows.
     * @param nbColumns the amount of desired columns.
     */
    public TorusGenerator(int nbRows, int nbColumns) {
        super(nbRows, nbColumns);
    }

    @Override
    protected void addLinks(Topology tp, int nbRows, int nbColumns, Node[][] nodes) {
        super.addLinks(tp, nbRows, nbColumns, nodes);

        for (int i = 0; i < nbRows; i++)
            tp.addLink(new Link(nodes[i][0], nodes[i][nbColumns - 1]));

        for (int j = 0; j < nbColumns; j++)
            tp.addLink(new Link(nodes[0][j], nodes[nbRows - 1][j]));

    }
}
