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
package io.jbotsim.topology;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.topology.generators.*;

/**
 * {@link TopologyGenerators} is a convenience class providing methods used to generate different patterns with default
 * values.
 */
public class TopologyGenerators {
    public static final String DEFAULT_NODE_MODEL = Topology.DEFAULT_NODE_MODEL_NAME;

    /**
     * Generates an horizontal line.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     */
    public static void generateLine(Topology topology, int nbNodes){
        generateLine(topology, nbNodes, DEFAULT_NODE_MODEL);
    }

    /**
     * Generates an horizontal line.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateLine(Topology topology, int nbNodes, String nodeModelName){
        LineGenerator generator = new LineGenerator(nbNodes, topology.getWidth(), true);
        generator.setAbsoluteCoords(true);
        generator.setX(50);
        generator.setY(100);
        generator.setWired(false);
        generator.setNodeClass(topology.getNodeModel(nodeModelName));
        generator.generate(topology);
    }

    /**
     * Generates a ring.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     */
    public static void generateRing(Topology topology, int nbNodes) {
        generateRing(topology, nbNodes, false, DEFAULT_NODE_MODEL);
    }

    /**
     * Generates a ring.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateRing(Topology topology, int nbNodes, String nodeModelName) {
        generateRing(topology, nbNodes, false, nodeModelName);
    }

    /**
     * Generates a ring.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     * @param directed {@code true} if the links should be directed; {@code false} otherwise.
     */
    public static void generateRing(Topology topology, int nbNodes, boolean directed) {
        generateRing(topology, nbNodes, directed, DEFAULT_NODE_MODEL);
    }

    /**
     * Generates a ring.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     * @param directed {@code true} if the links should be directed; {@code false} otherwise.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateRing(Topology topology, int nbNodes, boolean directed, String nodeModelName) {

        RingGenerator generator = new RingGenerator(nbNodes);
        generator.setAbsoluteCoords(true);
        generator.setX(50);
        generator.setY(100);
        generator.setWidth(200);
        generator.setHeight(200.0);
        generator.setWired(true);
        generator.setDirected(directed);
        generator.setNodeClass(topology.getNodeModel(nodeModelName));
        generator.generate(topology);

    }

    /**
     * Generates a grid.
     *
     * @param topology the related {@link Topology} object.
     * @param order the desired amount of {@link Node}s per row and per column.
     */
    public static void generateGrid(Topology topology, int order) {
        generateGrid(topology, order, DEFAULT_NODE_MODEL);
    }

    /**
     * Generates a grid.
     *
     * @param topology the related {@link Topology} object.
     * @param order the desired amount of {@link Node}s per row and per column.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateGrid(Topology topology, int order, String nodeModelName){
        generateGrid(topology, order, order, nodeModelName);
    }

    /**
     * Generates a grid.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodesRow the desired amount of {@link Node}s per row.
     * @param nbNodesColumn the desired amount of {@link Node}s per column.
     */
    public static void generateGrid(Topology topology, int nbNodesRow, int nbNodesColumn){
        generateGrid(topology, nbNodesRow, nbNodesColumn, DEFAULT_NODE_MODEL);
    }


    /**
     * Generates a grid.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodesRow the desired amount of {@link Node}s per row.
     * @param nbNodesColumn the desired amount of {@link Node}s per column.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateGrid(Topology topology, int nbNodesRow, int nbNodesColumn, String nodeModelName){

        GridGenerator generator = new GridGenerator(nbNodesRow, nbNodesColumn);
        applyDefaultGridGeneratorValues(topology, nodeModelName, generator);
        generator.generate(topology);

    }

    private static void applyDefaultGridGeneratorValues(Topology topology, String nodeModel, GridGenerator generator) {
        generator.setAbsoluteCoords(true);
        generator.setX(50);
        generator.setY(50);
        generator.setWidth(topology.getWidth() - 50);
        generator.setHeight(topology.getWidth() - 50);
        generator.setWired(false);
        generator.setNodeClass(topology.getNodeModel(nodeModel));
    }

    /**
     * Generates a torus.
     *
     * @param topology the related {@link Topology} object.
     * @param order the desired amount of {@link Node}s per row and per column.
     */
    public static void generateTorus(Topology topology, int order) {
        generateTorus(topology, order, DEFAULT_NODE_MODEL);
    }

    /**
     * Generates a torus.
     *
     * @param topology the related {@link Topology} object.
     * @param order the desired amount of {@link Node}s per row and per column.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateTorus(Topology topology, int order, String nodeModelName){

        TorusGenerator generator = new TorusGenerator(order, order);
        applyDefaultGridGeneratorValues(topology, nodeModelName, generator);
        generator.generate(topology);

    }
    
    /**
     * Generates a complete graph (Kn), as a ring.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     */
    public static void generateKN(Topology topology, int nbNodes){
        generateKN(topology, nbNodes, DEFAULT_NODE_MODEL);
    }

    /**
     * Generates a complete graph (Kn), as a ring.
     *
     * @param topology the related {@link Topology} object.
     * @param nbNodes the desired amount of {@link Node}s.
     * @param nodeModelName the model name under which the {@link Class} object (used to instantiate the
     * {@link Node}) is known in the provided {@link Topology}
     *                  (see {@link Topology#setNodeModel(String, Class)}).
     */
    public static void generateKN(Topology topology, int nbNodes, String nodeModelName){

        KNGenerator generator = new KNGenerator(nbNodes);
        generator.setAbsoluteCoords(true);
        generator.setX(150);
        generator.setY(150);
        generator.setWidth(200);
        generator.setHeight(200.0);
        generator.setWired(false);
        generator.setNodeClass(topology.getNodeModel(nodeModelName));
        generator.generate(topology);

    }
}
