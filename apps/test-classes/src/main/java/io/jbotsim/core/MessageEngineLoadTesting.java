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

package io.jbotsim.core;

import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This purpose of this class is to test the behavior of JBotSim messaging capabilities.</p>
 * <p>Here, we can set the number of nodes to be created and the number of nodes per connected component (wired links).
 * Each round, each node sends a message to all of its neighbor (discarded upon reception).</p>
 */
public class MessageEngineLoadTesting {
    public static void main(String[] args) {
        Topology topology = new Topology();
        Class<? extends Node> nodeClass = MyBroadcastNode.class;
        topology.setDefaultNodeModel(nodeClass);
        topology.setTimeUnit(1);

//        DelayMessageEngine messageEngine = new DelayMessageEngine(topology);
//        messageEngine.setDelay(250);
////        messageEngine.disableLinksContinuityChecks();
//        topology.setMessageEngine(messageEngine);

        topology.addClockListener(new TimeLoggingClockListener(topology));

        topology.setWirelessStatus(false);
        new KDeployer(topology, nodeClass).deploy(3000, 50);

        System.out.println("Nb nodes: " + topology.getNodes().size());
        new JViewer(topology);
        topology.start();

    }


    public static class MyBroadcastNode extends Node {

        @Override
        public void onStart() {
            super.onStart();
            setIconSize(2);
            setIcon(Icons.TRANSPARENT);
        }

        @Override
        public void onClock() {
            sendAll(new Message());
        }

    }

    private static class TimeLoggingClockListener implements ClockListener {
        private long previousTime;
        private Topology topology;

        public TimeLoggingClockListener(Topology topology) {
            this.previousTime = System.currentTimeMillis();
            this.topology = topology;
        }

        @Override
        public void onClock() {
            long newTime = System.currentTimeMillis();
            System.out.println(topology.getTime() + " " + (newTime - previousTime));
            previousTime = newTime;
        }
    }

    private static class KDeployer {
        private final Topology topology;
        private final Class<? extends Node> nodeClass;

        public KDeployer(Topology topology, Class<? extends Node> nodeClass) {
            this.topology = topology;
            this.nodeClass = nodeClass;
        }

        public void deploy(int totalNodes, int k) {
            int connectedComponents = (int)Math.round((double)totalNodes / k);
            System.out.println("Connected Components " + connectedComponents);
            for(int i = 0; i < connectedComponents; i++ )
                deployK(topology, k, nodeClass);
        }

        private static void deployK(Topology topology, int k, Class<? extends Node> nodeClass) {
            List<Node> nodes = new ArrayList();
            for(int i = 0 ; i < k ; i++) {
                try {
                    Node myNode = nodeClass.newInstance();
                    nodes.add(myNode);
                    topology.addNode(-1, -1, myNode);

                    connectAll(topology, nodes, myNode);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void connectAll(Topology topology, List<Node> nodes, Node myNode) {
            for(Node n: nodes)
                topology.addLink(new Link(myNode, n, Link.Orientation.UNDIRECTED, Link.Mode.WIRED));
        }
    }
}
