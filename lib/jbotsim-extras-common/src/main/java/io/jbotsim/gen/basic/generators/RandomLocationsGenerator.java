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
package io.jbotsim.gen.basic.generators;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.Random;

/**
 * The {@link RandomLocationsGenerator} is a {@link TopologyGenerator} used to create randomly-positioned {@link Node}s.
 */
public class RandomLocationsGenerator extends AbstractGenerator {
    private Random rnd = new Random();


    /**
     * Creates a {@link RandomLocationsGenerator} creating nbNodes randomly-positioned {@link Node}s.
     *
     * @param nbNodes the number of {@link Node}s to be added.
     */
    public RandomLocationsGenerator(int nbNodes) {
        setDefaultWidthHeight();
        setNbNodes(nbNodes);
    }

    @Override
    public void generate(Topology topology) {
        try {
            int nbNodes = getNbNodes();
            Node[] nodes = generateNodes(topology, nbNodes);

            if (wired) {
                Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;

                for (int i = 0; i < nbNodes; i++)
                    for (int j = i+1; j < nbNodes; j++)
                        if (rnd.nextDouble() > 0.2)
                            topology.addLink(new Link(nodes[i], nodes[j], type, Link.Mode.WIRED));

            }
        } catch (ReflectiveOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    private Node[] generateNodes (Topology tp, int nbNodes) throws ReflectiveOperationException {
        Node[] result = new Node[nbNodes];
        double w = getAbsoluteWidth(tp);
        double h = getAbsoluteHeight(tp);
        double x0 = getAbsoluteX(tp);
        double y0 = getAbsoluteY(tp);

        for(int i = 0; i < nbNodes; i++) {
            Node n = addNodeAtLocation(tp, x0 + rnd.nextDouble()*w, y0+ rnd.nextDouble()*h, -1);
            result[i] = n;
        }
        return result;
    }


    // region setter/getter
    @Override
    public AbstractGenerator setWidth(double width) {
        return super.setWidth(width);
    }

    @Override
    public AbstractGenerator setHeight(double height) {
        return super.setHeight(height);
    }

    @Override
    public double getWidth() {
        return super.getWidth();
    }

    @Override
    public double getHeight() {
        return super.getHeight();
    }
    // endregion
}
