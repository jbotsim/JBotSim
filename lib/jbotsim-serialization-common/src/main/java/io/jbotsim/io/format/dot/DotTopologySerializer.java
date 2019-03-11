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
package io.jbotsim.io.format.dot;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.io.TopologySerializer;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DotTopologySerializer implements TopologySerializer {
    private double scale;
    private int margin;

    public DotTopologySerializer() {
        this(2, 50);
    }

    public DotTopologySerializer(double scale, int margin) {
        this.scale = scale;
        this.margin = margin;
    }

    @Override
    public void importFromString(Topology topology, String data) {
        topology.disableWireless();

        GraphParser parser = new GraphParser(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        Map<String, GraphNode> nodes = parser.getNodes();
        if(nodes.isEmpty())
            return;

        Map<String, GraphEdge> edges = parser.getEdges();

        makeTopologyFromGraph(nodes, edges, topology);
        organize(topology, scale, margin);
    }

    @Override
    public String exportToString(Topology topology) {
        return null;
    }

    private void makeTopologyFromGraph(Map<String,GraphNode> nodes, Map<String,GraphEdge> edges, Topology tp) {
        HashMap<GraphNode,Node> nodeTable = new HashMap<>();

        for(String nname : nodes.keySet()) {
            GraphNode gn = nodes.get(nname);
            Node node = tp.newInstanceOfModel("default");
            extractAttributes(gn, node);
            tp.addNode(node);
            nodeTable.put(gn, node);
        }
        Link.Type linkType = Link.Type.UNDIRECTED;

        for(GraphEdge edge : edges.values()) {
            GraphNode gn1 = edge.getNode1();
            Node src = nodeTable.get(gn1);
            GraphNode gn2 = edge.getNode2();
            Node tgt = nodeTable.get(gn2);
            Link l = new Link(src, tgt, linkType);
            extractAttributes(edge, l);
            tp.addLink(l);
        }
    }

    private void extractAttributes(GraphNode mn, Node node) {
        String pos = (String) mn.getAttribute("pos");
        if (pos != null) {
            String[] xy = pos.split(",");
            node.setLocation(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
        }

        String c = (String) mn.getAttribute("color");
        if (c != null) {
            try {
                node.setColor(Color.decode(c.substring(0)));
            } catch (NumberFormatException e) {

            }
        }
        c = (String) mn.getAttribute("fillcolor");
        if (c != null) {
            try {
                node.setColor(Color.decode(c.substring(0)));
            } catch (NumberFormatException e) {

            }
        }
    }

    private void extractAttributes(GraphEdge ml, Link l) {
        String c = (String) ml.getAttribute("color");
        if (c != null) {
            try {
                l.setColor(Color.decode(c.substring(0)));
            } catch (NumberFormatException e) {

            }
        }
        String w = (String) ml.getAttribute("width");
        if (w != null) {
            try {
                l.setWidth(Integer.decode(w));
            } catch (NumberFormatException e) {

            }
        }
    }

    public static void organize(Topology tp, double scale, int margin){
        if(tp.getNodes().isEmpty())
            return;

        // Applies Scale
        for (Node node : tp.getNodes())
            node.setLocation(node.getX() * scale, node.getY() * scale);

        // Adjust window size and centers the graph within
        double minX = Integer.MAX_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxX = 0;
        double maxY = 0;
        for (Node node : tp.getNodes()){
            if (node.getX() < minX)
                minX = node.getX();
            if (node.getX() > maxX)
                maxX = node.getX();
            if (node.getY() < minY)
                minY = node.getY();
            if (node.getY() > maxY)
                maxY = node.getY();
        }
        double width = (maxX - minX) + 2*margin;
        double height = (maxY - minY) + 2*margin;
        tp.setDimensions((int) width, (int) height);
        double xshift = margin - minX;
        double yshift = margin - minY;
        for (Node node : tp.getNodes())
            node.setLocation(node.getX() + xshift, node.getY() + yshift);

        // Flips Y-coordinate
        for (Node node : tp.getNodes())
            node.setLocation(node.getX(), height - node.getY());
    }
}
