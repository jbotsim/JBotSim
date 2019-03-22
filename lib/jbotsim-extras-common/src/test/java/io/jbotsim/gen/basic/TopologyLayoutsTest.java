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
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class TopologyLayoutsTest {

    protected static final double EPSILON = 0.00000001;

    private static Node addNodeAtLocation(Topology topology, Point point) {
        Node node = new Node();
        node.setLocation(point);
        topology.addNode(node);
        return node;
    }

    private Topology generateSinglePointTopology(int width, int height) {
        Topology topology = new Topology(width, height);
        addNodeAtLocation(topology, new Point(12, 12, 12));
        return topology;
    }

    private Topology generateSeveralPointsOnSameLocationTopology(int width, int height, int nbNodes) {
        Topology topology = new Topology(width, height);
        for(int i = 0; i < nbNodes; i++)
            addNodeAtLocation(topology, new Point(12, 12, 12));
        return topology;
    }

    private static void checkCentered(Topology topology) {
        TopologyLayouts.TopologyBoundaries boundaries = TopologyLayouts.computeBoundaries(topology);

        Assertions.assertEquals(topology.getWidth() - boundaries.xMax, boundaries.xMin, EPSILON, "Not centered in width");
        Assertions.assertEquals(topology.getHeight() - boundaries.yMax, boundaries.yMin, EPSILON, "Not centered in height");
        // nothing to check for depth (no depth in Topology)
    }

    private static void markNodeHandled(HashMap<Integer, Boolean> idHandled, int nID) {
        idHandled.put(nID, true);
    }

    private static void checkAllIDHandled(int nbNodes, HashMap<Integer, Boolean> idHandled) {
        for(int i = 0; i < nbNodes; i++)
            Assertions.assertTrue(idHandled.get(i), "ID " + i + " not handled");
    }

    private static HashMap<Integer, Boolean> initIdHandledMap(int nbNodes) {
        HashMap<Integer, Boolean> idHandled = new HashMap<>();
        for(int i = 0; i < nbNodes; i++)
            idHandled.put(i, false);
        return idHandled;
    }

    private static void checkNodeLocation(Node testedNode, double expectedX, double expectedY, double expectedZ) {
        Assertions.assertEquals(expectedX, testedNode.getX(), EPSILON, "Incorrect X coordinate");
        Assertions.assertEquals(expectedY, testedNode.getY(), EPSILON, "Incorrect Y coordinate");
        Assertions.assertEquals(expectedZ, testedNode.getZ(), EPSILON, "Incorrect Z coordinate");
    }

    private static void checkTopologyDimensions(Topology topology, int expectedWidth, int expectedHeight) {
        Assertions.assertEquals(expectedWidth, topology.getWidth(), "Incorrect width");
        Assertions.assertEquals(expectedHeight, topology.getHeight(), "Incorrect height");
    }

    private static class TestTopology {

        protected static final int WIDTH = 200;
        protected static final int HEIGHT = 300;
        protected static final int FIXED_Z = 12;

        public static Topology generateTopology1() {
            Topology topology = new Topology(WIDTH, HEIGHT);

            addNodeAtLocation(topology, new Point(30, 60, FIXED_Z));
            addNodeAtLocation(topology, new Point(40, 50, FIXED_Z));

            return topology;
        }

        public static void assertScaledDefault1(Topology topology) {
            checkTopologyDimensions(topology, WIDTH, HEIGHT);
            checkCentered(topology);
            final int nbNodes = 2;
            HashMap<Integer, Boolean> idHandled = initIdHandledMap(nbNodes);

            for(Node n: topology.getNodes()) {
                int nID = n.getID();
                if(nID == 0) {
                    markNodeHandled(idHandled, nID);
                    checkNodeLocation(n, 20, 230, FIXED_Z);
                } else if (nID == 1) {
                    markNodeHandled(idHandled, nID);
                    checkNodeLocation(n, 180, 70, FIXED_Z);
                } else {
                    Assertions.fail("Unknown ID" + nID);
                }
            }

            checkAllIDHandled(nbNodes, idHandled);

        }

        public static void assertCenteredDefault1(Topology topology) {
            checkTopologyDimensions(topology, WIDTH, HEIGHT);
            checkCentered(topology);
            final int nbNodes = 2;
            HashMap<Integer, Boolean> idHandled = initIdHandledMap(nbNodes);

            for(Node n: topology.getNodes()) {
                int nID = n.getID();
                if(nID == 0) {
                    markNodeHandled(idHandled, nID);
                    checkNodeLocation(n, 95, 155, FIXED_Z);
                } else if (nID == 1) {
                    markNodeHandled(idHandled, nID);
                    checkNodeLocation(n, 105, 145, FIXED_Z);
                } else {
                    Assertions.fail("Unknown ID" + nID);
                }
            }

            checkAllIDHandled(nbNodes, idHandled);

        }
    }

    private static class TestHorizontalLine {
        protected static final int WIDTH = 200;
        protected static final int HEIGHT = 300;
        protected static final int FIXED_Z = 0;


        public static Topology generateTopology() {
            Topology topology = new Topology(WIDTH, HEIGHT);
            addNodeAtLocation(topology, new Point(12, 12, FIXED_Z));
            addNodeAtLocation(topology, new Point(12, 13, FIXED_Z));
            return topology;
        }

        public static void assertScaledDefault(Topology topology) {
            checkTopologyDimensions(topology, WIDTH, HEIGHT);
            checkCentered(topology);
            final int nbNodes = 2;
            HashMap<Integer, Boolean> idHandled = initIdHandledMap(nbNodes);

            for(Node n: topology.getNodes()) {
                int nID = n.getID();
                if(nID == 0) {
                    markNodeHandled(idHandled, nID);

                    checkNodeLocation(n, topology.getWidth()/2.0, 30, FIXED_Z);
                } else if (nID == 1) {
                    markNodeHandled(idHandled, nID);

                    checkNodeLocation(n, topology.getWidth()/2.0, 270, FIXED_Z);
                } else {
                    Assertions.fail("Unknown ID" + nID);
                }
            }

            checkAllIDHandled(nbNodes, idHandled);

        }
    }

    // region autoscale

    // region default
    @Test
    void autoscale_default_minimal_ok() {
        Topology topology = TestTopology.generateTopology1();

        TopologyLayouts.autoscale(topology);

        TestTopology.assertScaledDefault1(topology);
    }

    @Test
    void autoscale_default_emptyTopology_ok() {
        Topology topology = new Topology();

        TopologyLayouts.autoscale(topology);

        Assertions.assertTrue(topology.getNodes().isEmpty());
    }

    @Test
    void autoscale_default_single_centeredPoint() {
        int width = 200;
        int height = 300;
        Topology topology = generateSinglePointTopology(width, height);
        double zBefore = topology.getNodes().get(0).getZ();

        TopologyLayouts.autoscale(topology);

        checkNodeLocation(topology.getNodes().get(0), width/2.0, height/2.0, zBefore);
    }

    @Test
    void autoscale_default_severalOnSamePoint_allCentered() {
        int width = 200;
        int height = 300;
        int nbNodes = 30;
        Topology topology = generateSeveralPointsOnSameLocationTopology(width, height, nbNodes);
        double zBefore = topology.getNodes().get(0).getZ();

        TopologyLayouts.autoscale(topology);

        for(int i = 0; i < nbNodes; i++)
            checkNodeLocation(topology.getNodes().get(i), width/2.0, height/2.0, zBefore);
    }

    @Test
    void autoscale_default_line_ok() {
        Topology topology = TestHorizontalLine.generateTopology();

        TopologyLayouts.autoscale(topology);

        TestHorizontalLine.assertScaledDefault(topology);
    }
    // endregion default

    // region with params

    // TODO

    // endregion

    // endregion autoscale

    // region center

    @Test
    void center_emptyTopology_ok() {
        Topology topology = new Topology();

        TopologyLayouts.center(topology);

        Assertions.assertTrue(topology.getNodes().isEmpty());
    }

    @Test
    void center_single_centeredPoint() {
        int width = 200;
        int height = 300;
        Topology topology = generateSinglePointTopology(width, height);
        double zBefore = topology.getNodes().get(0).getZ();

        TopologyLayouts.center(topology);

        checkNodeLocation(topology.getNodes().get(0), width/2.0, height/2.0, zBefore);
    }

    @Test
    void center_severalOnSamePoint_allCentered() {
        int width = 200;
        int height = 300;
        int nbNodes = 30;
        Topology topology = generateSeveralPointsOnSameLocationTopology(width, height, nbNodes);
        double zBefore = topology.getNodes().get(0).getZ();

        TopologyLayouts.center(topology);

        for(int i = 0; i < nbNodes; i++)
            checkNodeLocation(topology.getNodes().get(i), width/2.0, height/2.0, zBefore);
    }

    @Test
    void center_minimal_ok() {
        Topology topology = TestTopology.generateTopology1();

        TopologyLayouts.center(topology);

        TestTopology.assertCenteredDefault1(topology);
    }

    // endregion


    // region computeBoundaries

    private void checkSameBoundaries(TopologyLayouts.TopologyBoundaries expectedBoundaries, TopologyLayouts.TopologyBoundaries boundaries) {
        check_xMin_boundary(expectedBoundaries.xMin, boundaries);
        check_yMin_boundary(expectedBoundaries.yMin, boundaries);
        check_zMin_boundary(expectedBoundaries.zMin, boundaries);
        check_xMax_boundary(expectedBoundaries.xMax, boundaries);
        check_yMax_boundary(expectedBoundaries.yMax, boundaries);
        check_zMax_boundary(expectedBoundaries.zMax, boundaries);
    }

    private void check_xMin_boundary(double expectedValue, TopologyLayouts.TopologyBoundaries boundaries) {
        Assertions.assertEquals(expectedValue, boundaries.xMin, EPSILON, "Incorrect xMin boundary");
    }

    private void check_yMin_boundary(double expectedValue, TopologyLayouts.TopologyBoundaries boundaries) {
        Assertions.assertEquals(expectedValue, boundaries.yMin, EPSILON, "Incorrect yMin boundary");
    }

    private void check_zMin_boundary(double expectedValue, TopologyLayouts.TopologyBoundaries boundaries) {
        Assertions.assertEquals(expectedValue, boundaries.zMin, EPSILON, "Incorrect zMin boundary");
    }

    private void check_xMax_boundary(double expectedValue, TopologyLayouts.TopologyBoundaries boundaries) {
        Assertions.assertEquals(expectedValue, boundaries.xMax, EPSILON, "Incorrect xMax boundary");
    }

    private void check_yMax_boundary(double expectedValue, TopologyLayouts.TopologyBoundaries boundaries) {
        Assertions.assertEquals(expectedValue, boundaries.yMax, EPSILON, "Incorrect yMax boundary");
    }

    private void check_zMax_boundary(double expectedValue, TopologyLayouts.TopologyBoundaries boundaries) {
        Assertions.assertEquals(expectedValue, boundaries.zMax, EPSILON, "Incorrect zMax boundary");
    }

    @Test
    void topologyBoundaries_defaultValues() {

        TopologyLayouts.TopologyBoundaries boundaries = new TopologyLayouts.TopologyBoundaries();

        check_xMin_boundary(0, boundaries);
        check_yMin_boundary(0, boundaries);
        check_zMin_boundary(0, boundaries);
        check_xMax_boundary(0, boundaries);
        check_yMax_boundary(0, boundaries);
        check_zMax_boundary(0, boundaries);

    }

    @Test
    void computeBoundaries_emptyTopology_ok() {

        TopologyLayouts.TopologyBoundaries boundaries = TopologyLayouts.computeBoundaries(new Topology());

        TopologyLayouts.TopologyBoundaries expectedBoundaries = new TopologyLayouts.TopologyBoundaries();

        checkSameBoundaries(expectedBoundaries, boundaries);

    }

    @Test
    void computeBoundaries_basicEnvelope_ok() {

        TopologyLayouts.TopologyBoundaries expectedBoundaries = new TopologyLayouts.TopologyBoundaries();
        expectedBoundaries.xMin = -25;
        expectedBoundaries.yMin = -20;
        expectedBoundaries.zMin = -10;
        expectedBoundaries.xMax = -1;
        expectedBoundaries.yMax = 200;
        expectedBoundaries.zMax = 100;

        Topology topology = new Topology();
        addNodeAtLocation(topology, new Point(expectedBoundaries.xMin , 12, 15));
        addNodeAtLocation(topology, new Point(-6, expectedBoundaries.yMin, expectedBoundaries.zMin));
        addNodeAtLocation(topology, new Point(expectedBoundaries.xMax , 25, 42.2));
        addNodeAtLocation(topology, new Point(-7, expectedBoundaries.yMax, 3.14));
        addNodeAtLocation(topology, new Point(-3, 0, expectedBoundaries.zMax));
        addNodeAtLocation(topology, new Point(-2, 0, 0));

        TopologyLayouts.TopologyBoundaries boundaries = TopologyLayouts.computeBoundaries(topology);

        checkSameBoundaries(expectedBoundaries, boundaries);

    }

    // endregion
}