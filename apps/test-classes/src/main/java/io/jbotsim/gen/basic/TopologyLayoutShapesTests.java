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

import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.generators.RandomLocationsGenerator;
import io.jbotsim.ui.JViewer;

public class TopologyLayoutShapesTests {

    protected static final double MARGIN_RATIO = 0.1;

    public static void main(String[] args) {
        int nbNodes = 20;

        // Generate a random Topology
        Topology topology = generateRandomTopology(nbNodes);

        applyCircleShape(topology);
//        applyEllipseShape(topology);
//        applyLineShape(topology);

        new JViewer(topology);
        topology.start();
    }

    private static void applyCircleShape(Topology topology) {
        TopologyLayouts.circle(topology);
//        TopologyLayouts.circle(topology, MARGIN_RATIO);
    }

    private static void applyEllipseShape(Topology topology) {
        TopologyLayouts.ellipse(topology);
//        TopologyLayouts.ellipse(topology, MARGIN_RATIO, MARGIN_RATIO*2);
    }

    private static void applyLineShape(Topology topology) {
        TopologyLayouts.line(topology);
//        TopologyLayouts.line(topology, MARGIN_RATIO);
    }

    private static Topology generateRandomTopology(int nbNodes) {
        RandomLocationsGenerator generator = new RandomLocationsGenerator(nbNodes);
        Topology topology = new Topology(800, 600);
        generator.setX(0.1);
        generator.setY(0.1);
        generator.setHeight(0.5);
        generator.setWidth(0.5);
        generator.generate(topology);
        return topology;
    }
}
