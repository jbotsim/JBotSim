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

import java.io.PrintWriter;
import java.util.*;

/**
 * This class stores all data loaded from a DOT file.
 *  - the graph
 *  - global attributes for graphs, nodes and edges.
 */
class DotGraph extends AttributeTable {
    private boolean strict = false;
    private boolean directed = false;
    private String id = null;
    private AttributeTable nodeAttributes = new AttributeTable();
    private AttributeTable edgeAttributes = new AttributeTable();
    private List<DotNode> nodes = new ArrayList<>();
    private HashMap<String, DotNode> nodeTable = new HashMap<>();
    private List<DotEdge> edges = new ArrayList<>();
    private List<DotGraph> subgraphs = new ArrayList<>();

    DotGraph() {
        super();
    }

    String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    void addNode(DotNode node) {
        nodes.add(node);
        nodeTable.put(node.getId(), node);
    }

    DotNode getNode(String id) {
        DotNode res = nodeTable.getOrDefault(id, null);
        if (res != null)
            return res;
        for (DotGraph sg : subgraphs) {
            res = sg.getNode(id);
            if (res != null)
                break;
        }

        return res;
    }

    void addEdge(DotEdge edge) {
        edges.add(edge);
    }

    void addSubGraph(DotGraph subgraph) {
        subgraphs.add(subgraph);
    }

    Set<DotNode> getAllNodes() {
        return getAllNodes(new TreeSet<>((o1, o2) ->
                o1.getId().compareTo(o2.getId())));
    }

    Set<DotNode> getAllNodes(Set<DotNode> result) {
        result.addAll(nodes);
        for (DotGraph sg : subgraphs) {
            sg.getAllNodes(result);
        }

        return result;
    }

    List<DotEdge> getAllEdges() {
        return getAllEdges(new ArrayList<>());
    }

    List<DotEdge> getAllEdges(List<DotEdge> result) {
        result.addAll(edges);
        for (DotGraph sg : subgraphs) {
            sg.getAllEdges(result);
        }
        return result;
    }

    boolean isStrict() {
        return strict;
    }

    void setStrict(boolean strict) {
        this.strict = strict;
    }

    boolean isDirected() {
        return directed;
    }

    void setDirected(boolean directed) {
        this.directed = directed;
    }

    AttributeTable getNodeAttributes() {
        return nodeAttributes;
    }

    AttributeTable getEdgeAttributes() {
        return edgeAttributes;
    }

    @Override
    void prettyPrint(PrintWriter out) {
        out.println((isStrict() ? "strict " : "") +
                (isDirected() ? "digraph" : "graph") + " " +
                (getId() != null ? getId() : "") + "{");
        if (hasAttributes())
            out.println("  graph " + super.toString() + ";");
        if (nodeAttributes.hasAttributes())
            out.println("  node " + nodeAttributes.toString() + ";");
        if (edgeAttributes.hasAttributes())
            out.println("  edge " + edgeAttributes.toString() + ";");

        for (DotNode n : getAllNodes()) {
            out.print(" ");
            out.println(n.toString());
        }

        for (DotEdge e : getAllEdges()) {
            out.print(" ");
            out.println(e.toString());
        }

        out.println("}");
        out.flush();
    }
}
