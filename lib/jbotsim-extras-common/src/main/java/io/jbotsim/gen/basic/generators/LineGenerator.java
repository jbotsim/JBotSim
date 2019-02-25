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

/**
 * The {@link LineGenerator} is a {@link TopologyGenerator} used to create horizontal or vertical lines.
 */
public class LineGenerator extends AbstractGenerator {

    private boolean horizontal;

    /**
     * Creates a {@link LineGenerator} creating an horizontal line of size size with nbNodes {@link Node}s.
     *
     * @param nbNodes the number of {@link Node}s to be added.
     * @param size the size of the line
     */
    public LineGenerator(int nbNodes, int size) {
        this(nbNodes, size, true);
    }

    /**
     * Creates a {@link LineGenerator} creating a line of size size with nbNodes {@link Node}s.
     * The line orientation depends on the horizontal parameter.
     *
     * @param nbNodes the number of {@link Node}s to be added.
     * @param size the size of the line
     * @param horizontal {@code true} if the line should be horizontal, {@code false} if it should be vertical.
     */
    public LineGenerator(int nbNodes, int size, boolean horizontal) {
        this.horizontal = horizontal;
        setNbNodes(nbNodes);
        setSize(size);
    }

    @Override
    public void generate(Topology topology) {
        try {
            int nbNodes = getNbNodes();
            Node[] nodes = generateNodes(topology, nbNodes);
            if (wired) {
                Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
                for (int i = 1; i < nbNodes; i++) {
                    topology.addLink(new Link(nodes[i-1], nodes[i], type, Link.Mode.WIRED));
                }
            }
        } catch (ReflectiveOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    private Node[] generateNodes(Topology tp, int nbNodes) throws ReflectiveOperationException {
        Node[] result = new Node[nbNodes];

        double step = getSize() / (nbNodes + ((nbNodes > 1) ? -1 : 0));

        double cr = step +1;
        double dx = 0;
        double dy = 0;
        if (horizontal)
            dx = step;
        else
            dy = step;

        double x0 = getAbsoluteX(tp);
        double y0 = getAbsoluteY(tp);
        for(int i = 0; i < nbNodes; i++) {
            Node n = addNodeAtLocation(tp, x0, y0, cr);
            x0 += dx;
            y0 += dy;
            result[i] = n;
        }
        return result;
    }

    // region getter/setter

    /**
     * Sets the orientation state of the line.
     * @param horizontal {@code true} if the line should be horizontal, {@code false} otherwise.
     * @return a reference to the object.
     */
    public LineGenerator setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
        return this;
    }

    /**
     * Returns {@code true} if the line should be horizontal, {@code false} otherwise.
     * @return {@code true} if the line should be horizontal, {@code false} otherwise.
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    @Override
    public double getSize() {
        return super.getSize();
    }

    @Override
    public LineGenerator setSize(double size) {
        return (LineGenerator) super.setSize(size);
    }

    @Override
    public LineGenerator setNbNodes(Object dimensionIndex, int nbNodes) {
        return (LineGenerator) super.setNbNodes(dimensionIndex, nbNodes);
    }

    @Override
    public int getNbNodes() {
        return super.getNbNodes();
    }
    // endregion
}
