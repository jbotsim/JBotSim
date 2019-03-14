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

package io.jbotsim.gen.basic;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.List;

/**
 * <p>{@link TopologyLayouts} offers functions allowing to manipulate the content of a {@link Topology}:</p>
 * <ul>
 *     <li>{@link #center(Topology)}: centers the {@link Node}s of a {@link Topology}.</li>
 *     <li>{@link #autoscale(Topology)}: scales (up) elements ({@link Node}'s positions, communication ranges, sensing
 *     ranges) from a {@link Topology} to fill its boundaries at best. It also centers the elements inside the
 *     {@link Topology}. To do this, {@link #autoscale(Topology, AutoScaleParams)} is called with the default
 *     parameters.</li>
 *     <li>{@link #autoscale(Topology, AutoScaleParams)}:  scales (up) elements from a {@link Topology} as specified
 *     in the provided {@link AutoScaleParams} object.</li>
 * </ul>
 * <p>Some methods allow to modify the shape of the {@link Topology} (without respect with connectivity):
 * <ul>
 *     <li>as circles: {@link #circle(Topology)}, {@link #circle(Topology, double)}; </li>
 *     <li>as ellipses: {@link #ellipse(Topology)}, {@link #ellipse(Topology, double, double)};</li>
 *     <li>as lines: {@link #line(Topology)}, {@link #line(Topology, double)}.</li>
 * </ul>
 *
 * <p>The {@link #computeBoundaries(Topology)} provides a easy way to retrieve the provided {@link Topology}'s extreme
 * points, as a {@link TopologyBoundaries} object.</p>
 */
public class TopologyLayouts {
    public static final double DEFAULT_MARGIN = 10.0 / 100.0;

    // region auto-scale

    /**
     * The {@link AutoScaleParams} object is used to configure scaling operations.
     */
    public static class AutoScaleParams {
        /**
         * Default margin ratio: {@value #DEFAULT_AUTOSCALE_MARGIN_RATIO}.
         */
        public static final double DEFAULT_AUTOSCALE_MARGIN_RATIO = 0.1;
        /**
         * Default centering behaviour: {@value #DEFAULT_AUTOSCALE_CENTER}.
         */
        public static final boolean DEFAULT_AUTOSCALE_CENTER = true;
        /**
         * Default communication range scaling behaviour: {@value #DEFAULT_AUTOSCALE_COMMUNICATION_RANGE}.
         */
        public static final boolean DEFAULT_AUTOSCALE_COMMUNICATION_RANGE = true;
        /**
         * Default sensing range scaling behaviour: {@value #DEFAULT_AUTOSCALE_SENSING_RANGE}.
         */
        public static final boolean DEFAULT_AUTOSCALE_SENSING_RANGE = true;

        /**
         * The desired minimum scale factor of each margin on the X axis. A double in [0:1].
         */
        public double scaleMarginRatioX = DEFAULT_AUTOSCALE_MARGIN_RATIO;
        /**
         * The desired minimum scale factor of each margin on the Y axis. A double in [0:1].
         */
        public double scaleMarginRatioY = DEFAULT_AUTOSCALE_MARGIN_RATIO;
        /**
         * The desired minimum scale factor of each margin on the Z axis. A double in [0:1].
         */
        public double scaleMarginRatioZ = DEFAULT_AUTOSCALE_MARGIN_RATIO;

        /**
         * Specifies whether centering should be applied.
         */
        public boolean center = DEFAULT_AUTOSCALE_CENTER;

        /**
         * Specifies whether the communication range should be scaled.
         */
        public boolean scaleCommunicationRange = DEFAULT_AUTOSCALE_COMMUNICATION_RANGE;

        /**
         * Specifies whether the sensing range should be scaled.
         */
        public boolean scaleSensingRange = DEFAULT_AUTOSCALE_SENSING_RANGE;
    }

    /**
     * <p>Automatically scales the position of the {@link Node}s of the {@link Topology} to match its boundaries.</p>
     * <p>A margin ratio of {@value AutoScaleParams#DEFAULT_AUTOSCALE_MARGIN_RATIO} is used on all sides to keep boudaries clean.</p>
     * <p>For more control on the margins, please use {@link #autoscale(Topology, AutoScaleParams)}.</p>
     * <p>Note that this method changes the {@link Node}s' position without respect for their
     * {@link io.jbotsim.core.Link}s.</p>
     * @param topology the {@link Topology} to scale.
     */
    public static void autoscale(Topology topology){
        autoscale(topology, new AutoScaleParams());
    }

    /**
     * <p>Automatically scales the position of the {@link Node}s of the {@link Topology} to match its boundaries.</p>
     * <p>Note that this method changes the {@link Node}s' position without respect for their
     * {@link io.jbotsim.core.Link}s.</p>
     *
     * @param topology the {@link Topology} to scale.
     * @param autoScaleParams the {@link AutoScaleParams} object specifying the scaling process.
     */
    public static void autoscale(Topology topology, AutoScaleParams autoScaleParams){

        TopologyBoundaries boundaries = computeBoundaries(topology);

        double scaleFactor = computeScaleFactor(topology, boundaries, autoScaleParams);

        double newMarginX = topology.getWidth() * autoScaleParams.scaleMarginRatioX;
        double newMarginY = topology.getHeight() * autoScaleParams.scaleMarginRatioY;

        for (Node node : topology.getNodes()){
            double normalizedX = node.getX() - boundaries.xMin;
            double normalizedY = node.getY() - boundaries.yMin;

            node.setLocation(normalizedX * scaleFactor + newMarginX,
                    normalizedY * scaleFactor + newMarginY, node.getZ());
        }

        if(autoScaleParams.center)
            center(topology);

        if(autoScaleParams.scaleCommunicationRange)
            topology.setCommunicationRange(topology.getCommunicationRange() * scaleFactor);

        if(autoScaleParams.scaleSensingRange)
            topology.setSensingRange(topology.getSensingRange() * scaleFactor);
    }

    /**
     * <p>Automatically centers the {@link Node}s of the {@link Topology} inside its boundaries.</p>
     * <p>Note that nodes positions are supposed to be inside the boundaries of the {@link Topology}.</p>
     *
     * @param topology the {@link Topology} to center.
     */
    public static void center(Topology topology) {
        TopologyBoundaries boundaries = computeBoundaries(topology);

        double optimalMarginX = (topology.getWidth() - (boundaries.xMax - boundaries.xMin)) / 2;
        double shiftX = optimalMarginX - boundaries.xMin;

        double optimalMarginY = (topology.getHeight() - (boundaries.yMax - boundaries.yMin)) / 2;
        double shiftY = optimalMarginY - boundaries.yMin;

        // The topology has no "depth" dimension, so, nothing to center

        for(Node node :topology.getNodes())
            node.setLocation(node.getX() + shiftX, node.getY() + shiftY, node.getZ());

    }

    /**
     * The {@link TopologyLayouts} represents the boundaries of a topology.
     */
    public static class TopologyBoundaries {
        public double xMax = 0;
        public double yMax = 0;
        public double zMax = 0;
        public double xMin = 0;
        public double yMin = 0;
        public double zMin = 0;
    }

    /**
     * Computes the boundaries of a {@link Topology}.
     *
     * @param topology the {@link Topology} to be examined?
     * @return a {@link TopologyBoundaries}.
     */
    public static TopologyBoundaries computeBoundaries (Topology topology) {
        TopologyBoundaries boundaries = new TopologyBoundaries();

        if(topology.getNodes().isEmpty())
            return boundaries;

        boundaries.xMin = Double.MAX_VALUE;
        boundaries.yMin = Double.MAX_VALUE;
        boundaries.zMin = Double.MAX_VALUE;
        boundaries.xMax = -Double.MAX_VALUE;
        boundaries.yMax = -Double.MAX_VALUE;
        boundaries.zMax = -Double.MAX_VALUE;

        for (Node node : topology.getNodes()){
            if (node.getX() > boundaries.xMax)
                boundaries.xMax = node.getX();
            if (node.getY() > boundaries.yMax)
                boundaries.yMax = node.getY();
            if (node.getZ() > boundaries.zMax)
                boundaries.zMax = node.getZ();

            if (node.getX() < boundaries.xMin)
                boundaries.xMin = node.getX();
            if (node.getY() < boundaries.yMin)
                boundaries.yMin = node.getY();
            if (node.getZ() < boundaries.zMin)
                boundaries.zMin = node.getZ();
        }

        return boundaries;
    }

    protected static double computeScaleFactor(Topology topology, TopologyBoundaries boundaries, AutoScaleParams scaleParams) {
        double originalWidth = boundaries.xMax - boundaries.xMin;
        double originalHeight = boundaries.yMax - boundaries.yMin;
        double availableWidth = topology.getWidth() * computeAvailableSpaceRatio(scaleParams.scaleMarginRatioX);
        double availableHeight = topology.getHeight() * computeAvailableSpaceRatio(scaleParams.scaleMarginRatioZ);

        // XXX: Z coordinate is ignored

        if(originalWidth == 0 && originalHeight == 0)
            return 1;

        if(originalWidth == 0)
            return availableHeight / originalHeight;

        if(originalHeight == 0)
            return availableWidth / originalWidth;

        return Math.min(availableWidth / originalWidth, availableHeight/originalHeight);
    }

    protected static double computeAvailableSpaceRatio(double marginRatio) {
        return 1 - marginRatio * 2;
    }
    // endregion autoscale

    public static void circle(Topology tp, double margin) {
        List<Node> nodes = tp.getNodes();
        if (nodes.isEmpty())
            return;

        double w = tp.getWidth();
        double h = tp.getHeight();
        double xc = w / 2.0;
        double yc = h / 2.0;
        double radius = ((w < h ? w : h) * (1.0 - margin)) / 2.0;

        double angle = 0.0;
        double angleinc = 2.0 * Math.PI / nodes.size();
        for (Node n : nodes) {
            n.setLocation(xc + radius * Math.cos(angle), yc + radius * Math.sin(angle));
            angle += angleinc;
        }
    }

    public static void circle(Topology tp) {
        circle(tp, DEFAULT_MARGIN);
    }

    public static void ellipse(Topology tp, double xmargin, double ymargin) {
        List<Node> nodes = tp.getNodes();
        if (nodes.isEmpty())
            return;

        double w = tp.getWidth();
        double h = tp.getHeight();
        double xc = w / 2.0;
        double yc = h / 2.0;
        double xradius = w * (1.0 - xmargin) / 2.0;
        double yradius = h * (1.0 - ymargin) / 2.0;

        double angle = 0.0;
        double angleinc = 2.0 * Math.PI / nodes.size();
        for (Node n : nodes) {
            n.setLocation(xc + xradius * Math.cos(angle), yc + yradius * Math.sin(angle));
            angle += angleinc;
        }
    }

    public static void ellipse(Topology tp) {
        ellipse(tp, DEFAULT_MARGIN, DEFAULT_MARGIN);
    }

    public static void line(Topology tp, double margin) {
        List<Node> nodes = tp.getNodes();
        if (nodes.isEmpty())
            return;

        double w = tp.getWidth();
        double h = tp.getHeight();
        double x, y, dx, dy;

        if (w < h) {
            // vertical line
            x = w / 2.0;
            y = h * margin;
            dx = dy = 0.0;
            if (nodes.size() > 1)
                dy = h * (1.0 - 2.0 * margin) / (nodes.size() - 1);
        } else {
            // horizontal line
            x = w * margin;
            y = h / 2.0;
            dx = dy = 0.0;
            if (nodes.size() > 1)
                dx = w * (1.0 - 2.0 * margin) / (nodes.size() - 1);
        }

        for (Node n : nodes) {
            n.setLocation(x, y);
            x += dx;
            y += dy;
        }
    }

    public static void line(Topology tp) {
        line(tp, DEFAULT_MARGIN);
    }
}
