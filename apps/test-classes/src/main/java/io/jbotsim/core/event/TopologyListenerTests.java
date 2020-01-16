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

package io.jbotsim.core.event;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class TopologyListenerTests {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.setTimeUnit(1);

        for (int i=0; i < 2; i++)
            topology.addTopologyListener(new MyListener());

        Node node = new Node();
        topology.addNode(-1, -1, node);

        new JViewer(topology);
        topology.start();

        System.out.println("Please add and remove nodes");
    }

    private static class MyListener implements TopologyListener {
        private int count = 1;

        private void switchListener(Topology topology) {
            topology.removeTopologyListener(this);
            topology.addTopologyListener(new MyListener());
        }

        public void doIt(Node node, String action) {
            count--;

            System.out.println("["+toString()+"]: Node " + node + " " + action);
            if(count <= 0)
                switchListener(node.getTopology());
        }

        @Override
        public void onNodeAdded(Node node) {
            doIt(node, " added");
        }

        @Override
        public void onNodeRemoved(Node node) {
            doIt(node, " removed");
        }
    }
}
