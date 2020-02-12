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

package examples.basic.links;

import examples.basic.mobilebroadcast.MobileBroadcastNode;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.JDirectedLinkPainter;

/**
 * This examples adds a bunch of random nodes and some directed links between some of them.
 * The result is displayed with a JViewer.
 */
public class DirectedLinksMain {

    public static final int MAX_NODE_ID = 20;
    protected static final int NB_LINKS = 15;

    public static void main(String[] args) {
        Topology tp = new Topology();

        tp.setOrientation(Link.Orientation.DIRECTED);
        tp.disableWireless();

        deployRandomNodes(tp, MAX_NODE_ID);

        addRandomDirectedLinks(tp, NB_LINKS, MAX_NODE_ID);

        JViewer viewer = new JViewer(tp);
        viewer.getJTopology().setDefaultLinkPainter(new JDirectedLinkPainter());
        tp.start();

    }

    /**
     * Randomly deploy maxNodeId {@link Node}s in the {@link Topology}.
     *
     * @param tp the {@link Topology}
     * @param maxNodeId the upper bound of the identifiers available for {@link Node}s creation
     */
    private static void deployRandomNodes(Topology tp, int maxNodeId) {
        for (int i = 0; i < maxNodeId; i++){
            Node node = new MobileBroadcastNode();
            node.setID(i);
            tp.addNode(-1, -1, node);
        }
    }

    /**
     * Randomly add directed {@link Link}s between existing {@link Node}s in the {@link Topology}.
     *
     * @param tp the {@link Topology}
     * @param nbLinks the number of links to add
     * @param maxNodeId the upper bound of {@link Node} identifiers available in the {@link Topology}
     */
    private static void addRandomDirectedLinks(Topology tp, int nbLinks, int maxNodeId) {
        for(int i = 0; i < nbLinks; i++){
            Node from, to;
            do {
                from = pickRandomNode(tp, maxNodeId);
                to = pickRandomNode(tp, maxNodeId);
            } while (!isFitForNewDirectedLink(tp, from, to));

            addDirectedLink(tp, from, to);
        }
    }

    private static void addDirectedLink(Topology tp, Node from, Node to) {
        tp.addLink(new Link(from, to, Link.Orientation.DIRECTED, Link.Mode.WIRED));
    }

    private static boolean isFitForNewDirectedLink(Topology tp, Node from, Node to) {
        if(from == to)
            return false;

        if(tp.getLink(from, to, Link.Orientation.DIRECTED) != null)
            return false;

        return true;
    }

    private static Node pickRandomNode(Topology tp, int maxNodeId) {
        int randomIndex = (int) (Math.random() * maxNodeId);
        for(Node n : tp.getNodes())
            if(n.getID() == randomIndex)
                return n;

        return null;
    }

}
