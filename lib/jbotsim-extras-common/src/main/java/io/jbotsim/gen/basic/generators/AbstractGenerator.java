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

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link AbstractGenerator} holds common fields and methods for the different implementations of the
 * {@link TopologyGenerator} interface.
 */
public abstract class AbstractGenerator implements TopologyGenerator {


    protected static final int FIRST_DIMENSION_INDEX = 0;
    protected static final int SECOND_DIMENSION_INDEX = 1;

    // one dimension system
    private static final int SIZE_DIMENSION_INDEX = FIRST_DIMENSION_INDEX;

    // two dimension system
    private static final int HEIGHT_DIMENSION_INDEX = FIRST_DIMENSION_INDEX;
    private static final int WIDTH_DIMENSION_INDEX = SECOND_DIMENSION_INDEX;

    protected Map<Object, Double> dimensionsMap = new HashMap<>();

    protected boolean directed = false;
    protected boolean wired = false;
    protected boolean wirelessEnabled = true;

    // X coord in [0:1] interval
    protected double x = 0.2;
    // Y coord in [0:1] interval
    protected double y = 0.2;

    protected Map<Object, Integer> nbNodesMap = new HashMap<>();
    protected Class<? extends Node> nodeClass = null;
    protected boolean absoluteCoords = false;


    /**
     * Returns the absolute coordinates state. Namely:
     * <ul>
     *     <li>{@code true} if the provided coordinates are meant to be absolute</li>
     *     <li>{@code false} if the provided coordinates are meant to be in [0:1]</li>
     * </ul>
     *
     * @return the absolute coordinates.
     */
    public boolean isAbsoluteCoords() {
        return absoluteCoords;
    }

    /**
     * Sets the absolute coordinates state. Namely:
     * <ul>
     *     <li>{@code true} if the provided coordinates are meant to be absolute</li>
     *     <li>{@code false} if the provided coordinates are meant to be in [0:1]</li>
     * </ul>
     *
     * @param absoluteCoords the new absolute coordinates state.
     * @return a reference to the object.
     */
    public AbstractGenerator setAbsoluteCoords(boolean absoluteCoords) {
        this.absoluteCoords = absoluteCoords;
        return this;
    }

    /**
     * Returns {@code true} if the graph should be directed; {@code false} otherwise.
     *
     * @return {@code true} if the graph should be directed; {@code false} otherwise.
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * Sets the graph direction state.
     *
     * @param directed {@code true} if the graph should be directed; {@code false} otherwise.
     * @return a reference to the object.
     */
    public AbstractGenerator setDirected(boolean directed) {
        this.directed = directed;
        return this;
    }

    /**
     * Returns {@code true} if the links should be wired; {@code false} otherwise.
     *
     * @return {@code true} if the links should be wired; {@code false} otherwise.
     */
    public boolean isWired() {
        return wired;
    }


    /**
     * Sets the links' wiring state.
     *
     * @param wired {@code true} if the links should be wired; {@code false} otherwise.
     * @return a reference to the object.
     */
    public AbstractGenerator setWired(boolean wired) {
        this.wired = wired;
        return this;
    }

    /**
     * Returns {@code true} if the wireless links should be enabled; {@code false} otherwise.
     *
     * @return {@code true} if the wireless links should be enabled; {@code false} otherwise.
     */
    public boolean isWirelessEnabled() {
        return wirelessEnabled;
    }

    /**
     * Sets the links' wireless state.
     *
     * @param wirelessEnabled {@code true} if the wireless links should be enabled; {@code false} otherwise.
     * @return a reference to the object.
     */
    public AbstractGenerator setWirelessEnabled(boolean wirelessEnabled) {
        this.wirelessEnabled = wirelessEnabled;
        return this;
    }

    // region Coordinates

    /**
     * Gets the X coordinate.
     *
     * @return the X coordinate, as a double.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X coordinate.
     *
     * @param x the X coordinate, as a double.
     * @return a reference to the object.
     */
    public AbstractGenerator setX(double x) {
        this.x = x;
        return this;
    }

    /**
     * Returns the absolute X coordinate, depending on {@link #getX()} and {@link #isAbsoluteCoords()}.
     *
     * @param tp the related {@link Topology} object
     * @return the absolute X coordinate, as a double.
     */
    public double getAbsoluteX(Topology tp) {
        if (isAbsoluteCoords())
            return x;
        return x * tp.getWidth();
    }

    /**
     * Gets the Y coordinate.
     *
     * @return the Y coordinate, as a double.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y coordinate.
     *
     * @param y the Y coordinate, as a double.
     * @return a reference to the object.
     */
    public AbstractGenerator setY(double y) {
        this.y = y;
        return this;
    }

    /**
     * Returns the absolute Y coordinate, depending on {@link #getY()} and {@link #isAbsoluteCoords()}.
     *
     * @param tp the related {@link Topology} object
     * @return the absolute Y coordinate, as a double.
     */
    public double getAbsoluteY(Topology tp) {
        if (isAbsoluteCoords())
            return y;
        return y * tp.getHeight();
    }
    // endregion coordinates

    // region Dimensions

    /**
     * Sets the size of the generated pattern for an arbitrary dimension.
     *
     * @param dimensionIndex the dimension index; can be any type of object.
     * @param value          the value of the provided dimension.
     * @return a reference to the object.
     */
    protected AbstractGenerator setDimension(Object dimensionIndex, double value) {
        dimensionsMap.put(dimensionIndex, value);
        return this;
    }

    /**
     * Gets the size of the generated pattern for the provided dimension.
     *
     * @param dimensionIndex the dimension index; can be any type of object.
     * @return the size of the pattern for the provided dimension.
     */
    protected double getDimension(Object dimensionIndex) {
        return dimensionsMap.get(dimensionIndex);
    }

    /**
     * Gets the width of the generated pattern (2 dimensions).
     *
     * @return the width, as a double.
     */
    protected double getWidth() {
        return getDimension(WIDTH_DIMENSION_INDEX);
    }

    /**
     * Sets the width of the generated pattern (2 dimensions).
     *
     * @param width the desired width, as a double.
     * @return a reference to the object.
     */
    protected AbstractGenerator setWidth(double width) {
        setDimension(WIDTH_DIMENSION_INDEX, width);
        return this;
    }

    /**
     * Gets the absolute width of the generated pattern (2 dimensions).
     *
     * @param tp the related {@link Topology} object
     * @return the absolute width of the generated pattern
     */
    protected double getAbsoluteWidth(Topology tp) {
        if (isAbsoluteCoords())
            return getWidth();
        return getWidth() * tp.getWidth();
    }

    /**
     * Gets the height of the generated pattern (2 dimensions).
     *
     * @return the height, as a double.
     */
    protected double getHeight() {
        return getDimension(HEIGHT_DIMENSION_INDEX);
    }

    /**
     * Sets the height of the generated pattern (2 dimensions).
     *
     * @param height the desired height, as a double.
     * @return a reference to the object.
     */
    protected AbstractGenerator setHeight(double height) {
        setDimension(HEIGHT_DIMENSION_INDEX, height);
        return this;
    }

    /**
     * Gets the absolute height of the generated pattern (2 dimensions).
     *
     * @param tp the related {@link Topology} object
     * @return the absolute height of the generated pattern
     */
    protected double getAbsoluteHeight(Topology tp) {
        if (isAbsoluteCoords())
            return getHeight();
        return getHeight() * tp.getHeight();
    }

    protected void setDefaultWidthHeight() {
        setWidth(Topology.DEFAULT_WIDTH);
        setHeight(Topology.DEFAULT_HEIGHT);
    }

    /**
     * Gets the one-dimension size of the generated pattern.
     *
     * @return the size, as a double.
     */
    protected double getSize() {
        return getDimension(SIZE_DIMENSION_INDEX);
    }

    /**
     * Sets the one-direction size of the generated pattern.
     *
     * @param size the double representing of the generated pattern, as a double.
     * @return a reference to the object.
     */
    protected AbstractGenerator setSize(double size) {
        setDimension(SIZE_DIMENSION_INDEX, size);
        return this;
    }
    // endregion Dimensions

    /**
     * Gets the {@link Class} object with which {@link Node}s should be instantiated.
     *
     * @return the instantiated {@link Node} type.
     */
    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    /**
     * Gets the {@link Class} object with which {@link Node}s should be instantiated.
     *
     * If the class has not been set already been set with the generator's {@link #setNodeClass(Class)} method, the
     * topology's default Node model will be used.
     *
     * @param tp the {@link Topology} providing the backup Node model.
     * @return a reference to the object.
     */
    protected Class<? extends Node> getNodeClass(Topology tp) {
        Class<? extends Node> nodeClass = getNodeClass();
        if (nodeClass != null)
            return nodeClass;

        return tp.getDefaultNodeModel();
    }

    /**
     * Sets the {@link Class} object with which {@link Node}s should be instantiated.
     *
     * @param nodeClass the instantiated {@link Node} type.
     * @return a reference to the object.
     */
    public AbstractGenerator setNodeClass(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass == null ? Node.class : nodeClass;
        return this;
    }


    // region Number of Nodes

    /**
     * Sets the number of {@link Node}s which should be created in an arbitrary dimension.
     *
     * @param dimensionIndex the dimension index; can be any type of object.
     * @param nbNodes        the number of {@link Node} to be created.
     * @return a reference to the object.
     */
    protected AbstractGenerator setNbNodes(Object dimensionIndex, int nbNodes) {
        nbNodesMap.put(dimensionIndex, nbNodes);
        return this;
    }

    /**
     * Gets the number of {@link Node}s which should be created in an arbitrary dimension.
     *
     * @param dimensionIndex the dimension index; can be any type of object.
     * @return the number of {@link Node} to be created.
     */
    protected int getNbNodes(Object dimensionIndex) {
        return nbNodesMap.get(dimensionIndex);
    }


    /**
     * Gets the number of {@link Node}s which should be created (1-dimension).
     *
     * @return the number of {@link Node} to be created.
     */
    protected int getNbNodes() {
        return getNbNodes(FIRST_DIMENSION_INDEX);
    }

    /**
     * Sets the number of {@link Node}s which should be created (1-dimension).
     *
     * @param nbNodes the number of {@link Node} to be created.
     * @return a reference to the object.
     */
    protected AbstractGenerator setNbNodes(int nbNodes) {
        setNbNodes(FIRST_DIMENSION_INDEX, nbNodes);
        return this;
    }

    // endregion


    protected Node addNodeAtLocation(Topology tp, double x0, double y0, double cr) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        Node n = getNodeClass(tp).getConstructor().newInstance();
        n.setLocation(x0, y0);
        n.setWirelessStatus(wirelessEnabled);
        if (cr != -1)
            n.setCommunicationRange(cr);
        tp.addNode(n);
        return n;
    }
}
