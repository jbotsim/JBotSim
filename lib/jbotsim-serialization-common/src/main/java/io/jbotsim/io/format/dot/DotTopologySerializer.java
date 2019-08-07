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

import io.jbotsim.core.*;
import io.jbotsim.io.TopologySerializer;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DotTopologySerializer implements TopologySerializer {
    private static Map<String, Color> NAMES_TO_COLORS;
    private static Map<Color, String> COLORS_TO_NAMES;

    static {
        buildColorMaps();
    }

    private double scale;
    private int margin;
    private boolean reorganize;

    private static final String JBOTSIM_ATTR_NODE_CLASS = "jbotsimNodeClass";
    private static final String JBOTSIM_ATTR_IS_WIRELESS = "jbotsimIsWireless";
    private static final String JBOTSIM_ATTR_IS_DIRECTED = "jbotsimIsDirected";

    public static final double DEFAULT_SCALE = 1.0;
    public static final int DEFAULT_MARGIN = 50;

    public static final String[] DOT_FILENAME_EXTENSIONS = new String[]{"gv", "dot", "xdot"};

    public DotTopologySerializer() {
        this(DEFAULT_SCALE, DEFAULT_MARGIN);
    }

    public DotTopologySerializer(boolean reorganize) {
        this(DEFAULT_SCALE, DEFAULT_MARGIN, reorganize);
    }

    public DotTopologySerializer(double scale, int margin, boolean reorganize) {
        this.scale = scale;
        this.margin = margin;
        this.reorganize = reorganize;
    }

    public DotTopologySerializer(double scale, int margin) {
        this(scale, margin, true);
    }

    @Override
    public void importFromString(Topology topology, String data) {
        topology.disableWireless();
        try {
            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            DotGraph dotGraph = new GraphParser().parseGraph(is);

            makeTopologyFromGraph(dotGraph, topology);
            if (reorganize) {
                organize(topology, scale, margin);
            } else {
                int height = topology.getHeight();
                for (Node node : topology.getNodes())
                    node.setLocation(node.getX(), height - node.getY());
            }
        } catch (IOException | GraphParser.ParserException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String exportToString(Topology topology) {
        boolean resume = false;
        if (topology.isRunning()) {
            topology.pause();
            resume = true;
        }
        String edgeOp;
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        if (topology.isDirected()) {
            edgeOp = " -> ";
            out.print("digraph");
        } else {
            edgeOp = " -- ";
            out.print("graph");
        }
        out.println('{');
        int height = topology.getHeight();
        for (Node n : topology.getNodes()) {
            Color c = n.getColor();
            Point pos = n.getLocation();
            out.print("  " + n.getID() + " [");
            if (c != null) {
                out.print("style = \"filled\", fillcolor = \"" + getStringForColor(c) + "\", ");
            }
            out.print("pos = \"" + pos.getX() + "," + (height - pos.getY()) + "!\", ");
            out.print("width = \"" + n.getIconSize() + "\", ");
            out.print("height = \"" + n.getIconSize() + "\", ");
            out.print(JBOTSIM_ATTR_NODE_CLASS + " = \"" + n.getClass().getName() + "\", ");
            out.print("label = \"" + n.getID() + "\"");
            out.println("];");
        }

        for (int i = 0; i < 2; i++) {
            List<Link> links = topology.getLinks(Link.Orientation.DIRECTED);
            for (Link l : links) {
                int src = 0;
                if (!l.isDirected() && l.endpoint(1).getID() < l.endpoint(0).getID())
                    src = 1;
                out.print(l.endpoint(src).getID());
                out.print(edgeOp);
                out.print(l.endpoint((src + 1) % 2));
                out.print(" [");
                Integer w = l.getWidth();
                if (w.equals(Link.DEFAULT_WIDTH)) {
                    out.print("width = \"" + w + "\", ");
                }
                Color c = l.getColor();
                if (!Link.DEFAULT_COLOR.equals(c)) {
                    out.print("color = \"" + getStringForColor(c) + "\", ");
                }
                out.print(JBOTSIM_ATTR_IS_WIRELESS + " = \"" + (l.isWireless() ? 1 : 0) + "\", ");
                out.print(JBOTSIM_ATTR_IS_DIRECTED + " = \"" + (l.isDirected() ? 1 : 0) + "\", ");
                out.print("style = \"" + (l.isWireless() ? "dashed" : "solid") + "\"");
                out.println("];");
            }
        }
        out.println('}');
        out.flush();
        if (resume)
            topology.resume();
        return str.getBuffer().toString();
    }

    private void makeTopologyFromGraph(DotGraph graph, Topology tp) {
        HashMap<DotNode, Node> nodeTable = new HashMap<>();
        boolean enableWireless = false;

        for (DotNode dn : graph.getAllNodes()) {
            Node node;
            String className = dn.getAttribute(JBOTSIM_ATTR_NODE_CLASS);
            if (className == null) {
                node = tp.newInstanceOfModel(Topology.DEFAULT_NODE_MODEL_NAME);
            } else {
                try {
                    Class<? extends Node> nodeClass = Class.forName(className).asSubclass(Node.class);
                    node = nodeClass.newInstance();
                } catch (Exception e) {
                    node = tp.newInstanceOfModel(Topology.DEFAULT_NODE_MODEL_NAME);
                }
            }
            extractAttributes(dn, node);
            try {
                node.setID(Integer.parseInt(dn.getId()));
            } catch (NumberFormatException e) {
                node.setLabel(dn.getId());
            }
            tp.addNode(node);
            nodeTable.put(dn, node);
        }

        for (DotEdge edge : graph.getAllEdges()) {
            String isWireless = edge.getAttribute(JBOTSIM_ATTR_IS_WIRELESS);
            if (isWireless != null && Integer.parseInt(isWireless) == 1) {
                enableWireless = true;
                continue;
            }

            DotNode gn1 = edge.getNode1();
            Node src = nodeTable.get(gn1);
            DotNode gn2 = edge.getNode2();
            Node tgt = nodeTable.get(gn2);

            Link.Orientation linkOrientation = Link.Orientation.UNDIRECTED;
            String isDirected = (String) edge.getAttribute(JBOTSIM_ATTR_IS_DIRECTED);

            if (isDirected != null && Integer.parseInt(isDirected) == 1 ||
                edge.isDirected()) {
                linkOrientation = Link.Orientation.DIRECTED;
            }

            if (linkOrientation.equals(Link.Orientation.UNDIRECTED)) {
                if (src.getNeighbors().contains(tgt))
                    continue;

                if (src.getID() > tgt.getID()) {
                    Node tmp = src;
                    src = tgt;
                    tgt = tmp;
                }
            } else if (src.getOutLinkTo(tgt) != null) {
                continue;
            }
            Link l = new Link(src, tgt, linkOrientation);
            extractAttributes(edge, l);
            tp.addLink(l);
        }
        tp.setWirelessStatus(enableWireless);
    }

    private void extractAttributes(DotNode mn, Node node) {
        String pos = mn.getAttribute("pos");
        if (pos != null) {
            String[] xy = pos.replace("!", "").split(",");
            node.setLocation(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
        }

        String w = mn.getAttribute("width");
        if (w != null) {
            try {
                node.setIconSize(Integer.valueOf(w));
            } catch (NumberFormatException ignored) {

            }
        } else {
            String h = mn.getAttribute("height");
            try {
                node.setIconSize(Integer.valueOf(h));
            } catch (NumberFormatException ignored) {

            }
        }


        String c = mn.getAttribute("color");
        if (c != null) {
            try {
                node.setColor(getColorFromString(c));
            } catch (NumberFormatException ignored) {

            }
        }
        c = mn.getAttribute("fillcolor");
        if (c != null) {
            try {
                node.setColor(getColorFromString(c));
            } catch (NumberFormatException ignored) {

            }
        }
    }

    private void extractAttributes(DotEdge ml, Link l) {
        String c = ml.getAttribute("color");
        if (c != null) {
            try {
                l.setColor(getColorFromString(c));
            } catch (NumberFormatException ignored) {

            }
        }
        String w = ml.getAttribute("width");
        if (w != null) {
            try {
                l.setWidth(Integer.decode(w));
            } catch (NumberFormatException ignored) {

            }
        }
    }

    public static void organize(Topology tp, double scale, int margin) {
        if (tp.getNodes().isEmpty())
            return;

        // Applies Scale
        for (Node node : tp.getNodes())
            node.setLocation(node.getX() * scale, node.getY() * scale);

        // Adjust window size and centers the graph within
        double minX = Integer.MAX_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxX = 0;
        double maxY = 0;
        for (Node node : tp.getNodes()) {
            int sz = node.getIconSize();
            if (node.getX() - sz < minX)
                minX = node.getX() - sz;
            if (node.getX() + sz > maxX)
                maxX = node.getX() + sz;
            if (node.getY() - sz < minY)
                minY = node.getY() - sz;
            if (node.getY() + sz > maxY)
                maxY = node.getY() + sz;
        }
        double width = (maxX - minX) + 2 * margin;
        double height = (maxY - minY) + 2 * margin;
        tp.setDimensions((int) width, (int) height);
        double xshift = margin - minX;
        double yshift = margin - minY;
        for (Node node : tp.getNodes())
            node.setLocation(node.getX() + xshift, node.getY() + yshift);

        // Flips Y-coordinate
        for (Node node : tp.getNodes())
            node.setLocation(node.getX(), height - node.getY());

        tp.setCommunicationRange(tp.getCommunicationRange() * scale);
        tp.setSensingRange(tp.getSensingRange() * scale);
    }

    private static Color getColorFromString(String s) {
        Color result = NAMES_TO_COLORS.get(s);
        if (result == null) {
            result = Color.decode(s);
        }
        return result;
    }

    private static String getStringForColor(Color c) {
        String result = COLORS_TO_NAMES.get(c);
        if (result == null) {
            result = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        }
        return result;
    }

    private static final void buildColorMaps() {
        NAMES_TO_COLORS = new HashMap<>();
        COLORS_TO_NAMES = new HashMap<>();

        for (Field field : Color.class.getDeclaredFields()) {
            if (Color.class.equals(field.getType())) {
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                    String fieldName = field.getName();
                    try {
                        Color c = (Color) field.get(Color.class);
                        NAMES_TO_COLORS.put(fieldName, c);
                        COLORS_TO_NAMES.put(c, fieldName);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("buildColorMap: " + e.getMessage());
                    }
                }
            }
        }
    }
}
