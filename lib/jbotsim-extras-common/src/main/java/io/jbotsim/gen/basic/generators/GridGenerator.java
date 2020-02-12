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
 * The {@link GridGenerator} is a {@link TopologyGenerator} used to created grids of {@link Node}s.
 */
public class GridGenerator extends AbstractGenerator {

    /**
     * Creates a {@link GridGenerator} creating a grid with nbRows rows and nbColumns columns.

     * @param nbRows the amount of desired rows.
     * @param nbColumns the amount of desired columns.
     */
    public GridGenerator(int nbRows, int nbColumns) {
        setDefaultWidthHeight();

        setNbNodes(FIRST_DIMENSION_INDEX, nbRows);
        setNbNodes(SECOND_DIMENSION_INDEX, nbColumns);
    }

    @Override
    public void generate(Topology topology) {
        try {
            generateGrid(topology);
        } catch (ReflectiveOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    protected Node[][] generateGrid(Topology tp) throws ReflectiveOperationException {
        int nbRows = getNbNodes(FIRST_DIMENSION_INDEX);
        int nbColumns = getNbNodes(SECOND_DIMENSION_INDEX);

        Node[][] nodes = generateNodes(tp, nbRows, nbColumns);

        addLinks(tp, nbRows, nbColumns, nodes);
        return nodes;
    }

    protected void addLinks(Topology tp, int nbRows, int nbColumns, Node[][] nodes) {
        if(wired) {
            Link.Orientation orientation = directed ? Link.Orientation.DIRECTED : Link.Orientation.UNDIRECTED;
            for (int i = 0; i < nbRows; i++) {
                for (int j = 0; j < nbColumns; j++) {
                    Node n = nodes[i][j];
                    if (i < nbRows - 1) {
                        Link l = new Link(n, nodes[i + 1][j], orientation);
                        tp.addLink(l);
                    }
                    if (j < nbColumns - 1) {
                        Link l = new Link(n, nodes[i][j + 1], orientation);
                        tp.addLink(l);
                    }
                }
            }
        }
    }

    private Node[][] generateNodes(Topology tp, int nbRows, int nbColumns) throws ReflectiveOperationException {
        Node[][] result = new Node[nbRows][];
        double x0 = getAbsoluteX(tp);
        double y0 = getAbsoluteY(tp);
        double xStep = getAbsoluteWidth(tp) / (nbRows>1?nbRows-1:nbRows);
        double yStep = getAbsoluteHeight(tp) / (nbColumns>1?nbColumns-1:nbColumns);
        double cr = Math.max(xStep, yStep) + 1;

        for (int i = 0; i < nbRows; i++) {
            result[i] = new Node[nbColumns];
            for (int j = 0; j < nbColumns; j++) {
                Node n = addNodeAtLocation(tp, x0 + i * xStep, y0 + j * yStep, cr);
                result[i][j] = n;
            }
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
