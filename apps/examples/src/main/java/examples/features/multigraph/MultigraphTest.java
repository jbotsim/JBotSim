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

package examples.features.multigraph;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.ui.JViewer;

/**
 * This class shows how JBotSim handles multigraphs
 */
public class MultigraphTest {
    private final Topology topology;

    public MultigraphTest(Link.Orientation orientation) {

        topology = new Topology();
        topology.setOrientation(orientation);

        Node n1 = addNode(topology, 50, 50);
        Node n2 = addNode(topology, 80, 50);

        // This link duplicates the automatically generated wireless one
        Link wirelessLink = new Link(n1, n2, orientation, Link.Mode.WIRELESS);
        topology.addLink(wirelessLink);

        // This wired link is added twice
        Link wiredLink = new Link(n1, n2, orientation, Link.Mode.WIRED);
        topology.addLink(wiredLink);
        topology.addLink(wiredLink);

        // Another wired link which duplicates the previous one
        Link wiredLink1 = new Link(n1, n2, orientation, Link.Mode.WIRED);
        topology.addLink(wiredLink1);

        // Loops
        topology.addLink(new Link(n1, n1, orientation, Link.Mode.WIRED));
        topology.addLink(new Link(n1, n1, orientation, Link.Mode.WIRELESS));

        new JViewer(topology);

        addCommandListener(topology);
        listLinks(topology);

    }

    public void run() {
        topology.start();
    }

    private Node addNode(Topology topology, double x, double y) {
        Node n2 = new Node();
        n2.setLocation(x, y);
        topology.addNode(n2);
        return n2;
    }

    private void addCommandListener(Topology topology) {
        String listLinksCommand = "List links";
        topology.addCommand(listLinksCommand);
        topology.addCommandListener(new ListLinksCommandListener(listLinksCommand, topology));
    }

    private class ListLinksCommandListener implements CommandListener {
        private final String listLinksCommand;
        private final Topology topology;

        public ListLinksCommandListener(String listLinksCommand, Topology topology) {
            this.listLinksCommand = listLinksCommand;
            this.topology = topology;
        }

        @Override
        public void onCommand(String command) {
            if(listLinksCommand.equalsIgnoreCase(command))
                listLinks(topology);
        }
    }

    private static void listLinks(Topology topology) {
        for (Link link : topology.getLinks())
            printLink(link);
        System.out.println();
    }

    private static void printLink(Link link) {
        Node source = link.source;
        Node destination = link.destination;
        Link.Orientation orientation = link.orientation;
        String edgeString = (orientation == Link.Orientation.UNDIRECTED)? " <--> " : " --> ";
        Link.Mode mode = link.mode;

        System.out.println("[" + link.hashCode() + "] " + source + edgeString + destination + " " + mode);
    }
}
