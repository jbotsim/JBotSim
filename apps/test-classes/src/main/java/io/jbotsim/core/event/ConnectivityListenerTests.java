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

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class ConnectivityListenerTests {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.setTimeUnit(1);

        for (int i=0; i < 2; i++)
            topology.addConnectivityListener(new MyListener());

        Node node1 = new Node();
        Node node2 = new Node();
        topology.addNode(-1, -1, node1);
        topology.addNode(-1, -1, node2);

        new JViewer(topology);
        topology.start();

        System.out.println("Please add and remove links by moving the nodes");
    }

    private static class MyListener implements ConnectivityListener {
        private int count = 1;

        private void switchListener(Topology topology) {
            topology.removeConnectivityListener(this, Link.Orientation.DIRECTED);
            topology.removeConnectivityListener(this, Link.Orientation.UNDIRECTED);
            topology.addConnectivityListener(new MyListener(), Link.Orientation.DIRECTED);
            topology.addConnectivityListener(new MyListener(), Link.Orientation.UNDIRECTED);
        }

        public void doIt(Link link, String action) {
            count--;

            System.out.println("["+toString()+"]: Node " + link + " " + action);
            if(count <= 0)
                switchListener(link.getTopology());
        }

        @Override
        public void onLinkAdded(Link link) {
            doIt(link, " added");
        }

        @Override
        public void onLinkRemoved(Link link) {
            doIt(link, " removed");
        }
    }
}
