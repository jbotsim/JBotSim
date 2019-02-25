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
 * <p>The {@link RingGenerator} is a {@link TopologyGenerator} used to create a ring.</p>
 *
 * <p>The size of the generated ring is defined by its width ({@link #setWidth(double)}) and
 * height ({@link #setHeight(double)}).</p>
 */
public class RingGenerator extends AbstractGenerator {

    /**
     * Creates a {@link RingGenerator} creating a ring of nbNodes {@link Node}s.
     *
     * @param nbNodes the number of {@link Node}s to be added.
     */
    public RingGenerator(int nbNodes) {
        setDefaultWidthHeight();
        setNbNodes(nbNodes);
    }

    @Override
    public void generate(Topology topology) {
        try {
            int nbNodes = getNbNodes();
            Node[] nodes = generateNodes(topology, nbNodes);

            addLinks(topology, nbNodes, nodes);
        } catch (ReflectiveOperationException e) {
            System.err.println(e.getMessage());
        }
    }


    protected Node[] generateNodes(Topology tp, int nbNodes) throws ReflectiveOperationException {
        Node[] nodes = new Node[nbNodes];
        double arc = Math.PI * 2.0 / nbNodes;
        double x0 = getAbsoluteX(tp);
        double y0 = getAbsoluteY(tp);
        double xrad = getAbsoluteWidth(tp) / 2.0;
        double yrad = getAbsoluteHeight(tp) / 2.0;

        double angle = 0.0;
        for (int i = 0; i < nbNodes; i++) {
            Node n = addNodeAtLocation(tp, x0 + xrad * Math.cos(angle), y0 + yrad * Math.sin(angle), -1);
            angle += arc;
            nodes[i] = n;
        }

        return nodes;
    }

    protected void addLinks(Topology tp, int nbNodes, Node[] nodes) {
        if (wired) {
            Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
            for (int i = 1; i < nbNodes; i++)
                tp.addLink(new Link(nodes[i - 1], nodes[i], type));
            tp.addLink(new Link(nodes[nbNodes - 1], nodes[0], type));
        } else {
            for (int i = 0; i < nbNodes; i++) {
                Node pred = (i == 0) ? nodes[nbNodes - 1] : nodes[i - 1];
                Node next = (i == nbNodes - 1) ? nodes[0] : nodes[i + 1];
                Node n = nodes[i];
                n.setCommunicationRange(1+Math.max(n.distance(pred), n.distance(next)));
            }
        }
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
