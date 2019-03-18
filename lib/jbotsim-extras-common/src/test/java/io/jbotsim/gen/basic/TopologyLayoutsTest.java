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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class TopologyLayoutsTest {

    protected static final double EPSILON = 0.00000001;

    // region autoscale
    @Test
    void autoscale_minimal_ok() {
        Topology topology = TestTopology.generateTopology1();

        TopologyLayouts.autoscale(topology);

        TestTopology.assertScaledTopology1(topology);
    }

    @Test
    void autoscale_emptyTopology_ok() {
        Topology topology = new Topology();

        TopologyLayouts.autoscale(topology);

        Assertions.assertTrue(topology.getNodes().isEmpty());
    }

    @Test
    void autoscale_single_NaN() {
        // FIXME: should not have NaN here
        Topology topology = new Topology();
        topology.addNode(12,12);

        TopologyLayouts.autoscale(topology);

        checkNodeLocation(topology.getNodes().get(0), Double.NaN, Double.NaN);
    }

    @Test
    void autoscale_severalOnSame() {
        // FIXME: should not have NaN here
        Topology topology = new Topology();
        for(int i = 0; i < 30; i++)
            topology.addNode(12,12);

        TopologyLayouts.autoscale(topology);

        for(int i = 0; i < 30; i++)
            checkNodeLocation(topology.getNodes().get(i), Double.NaN, Double.NaN);
    }

    @Test
    void autoscale_line_ok() {
        Topology topology = TestHorizontalLine.generateTopology();

        TopologyLayouts.autoscale(topology);

        TestHorizontalLine.assertScaled(topology);
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


    private static void checkNodeLocation(Node testedNode, double expectedX, double expectedY) {
        Assertions.assertEquals(expectedX, testedNode.getX(), EPSILON, "Incorrect X coordinate");
        Assertions.assertEquals(expectedY, testedNode.getY(), EPSILON, "Incorrect Y coordinate");
    }

    private static class TestTopology {
        public static Topology generateTopology1() {
            Topology topology = new Topology(200, 300);

            topology.addNode(30, 60);//0
            topology.addNode(40, 50);//1

            return topology;
        }

        public static void assertScaledTopology1(Topology topology) {
            final int nbNodes = 2;
            HashMap<Integer, Boolean> idHandled = initIdHandledMap(nbNodes);

            for(Node n: topology.getNodes()) {
                int nID = n.getID();
                if(nID == 0) {
                    markNodeHandled(idHandled, nID);
                    checkNodeLocation(n, 20, 190);
                } else if (nID == 1) {
                    markNodeHandled(idHandled, nID);
                    checkNodeLocation(n, 180, 30);
                } else {
                    Assertions.fail("Unknown ID" + nID);
                }
            }

            checkAllIDHandled(nbNodes, idHandled);

        }
    }

    private static class TestHorizontalLine {
        public static Topology generateTopology() {
            Topology topology = new Topology(200, 300);
            topology.addNode(12,12);
            topology.addNode(12,13);
            return topology;
        }

        public static void assertScaled(Topology topology) {
            final int nbNodes = 2;
            HashMap<Integer, Boolean> idHandled = initIdHandledMap(nbNodes);

            for(Node n: topology.getNodes()) {
                int nID = n.getID();
                if(nID == 0) {
                    markNodeHandled(idHandled, nID);

                    checkNodeLocation(n, 20, 30);
                } else if (nID == 1) {
                    markNodeHandled(idHandled, nID);

                    checkNodeLocation(n, 20, 270);
                } else {
                    Assertions.fail("Unknown ID" + nID);
                }
            }

            checkAllIDHandled(nbNodes, idHandled);

        }
    }

    // endregion autoscale
}