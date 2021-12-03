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

package io.jbotsim.gen.basic.generators;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

/**
 * The {@link KNGenerator} is a {@link TopologyGenerator} used to created complete-graphs shaped as rings.
 */
public class KNGenerator extends RingGenerator {

    protected static final double EPSILON = 0.01;

    /**
     * Creates a {@link KNGenerator} creating a complete-graph of nbNodes {@link Node}s.
     *
     * @param nbNodes the number of {@link Node}s to be added.
     */
    public KNGenerator(int nbNodes) {
        super(nbNodes);
    }

    @Override
    protected void addLinks(Topology tp, int nbNodes, Node[] nodes) {

        if (wired) {
            for (int i = 0; i < nbNodes; i++)
                for (int j = i + 1; j < nbNodes; j++)
                    tp.addLink(new Link(nodes[i], nodes[j], Link.Orientation.UNDIRECTED));
        } else {
            double cr = Math.max(getAbsoluteHeight(tp), getAbsoluteWidth(tp)) + EPSILON;
            for (Node n : nodes) {
                n.setCommunicationRange(cr);
            }
        }
    }
}
