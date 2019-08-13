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

package io.jbotsim.io.format.graph6;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * This class gathers mains functions used to encode or decode a Graph6/Digraph6
 * input string. It uses {@link Graph6BVHelper} API to produce or read byte buffers
 * specified in <a href="https://users.cecs.anu.edu.au/~bdm/data/formats.txt">reference document</a>.
 */
class Graph6Codec {
    // region Header and prefixes used by Graph6 format
    private static final String GRAPH6_HEADER = ">>graph6<<";
    private static final String GRAPH6_STARTER = "";
    private static final String SPARSE6_HEADER = ">>sparse6<<";
    private static final String SPARSE6_STARTER = ":";
    private static final String SPARSE6_INCREMENTAL_STARTER = ";";
    private static final String DIGRAPH6_HEADER = ">>digraph6<<";
    private static final String DIGRAPH6_STARTER = "&";
    // endregion Header and prefixes used by Graph6 format

    // region API to package
    public static String encodeGraph(Topology tp, boolean withHeader) {
        Node[] nodes = tp.getNodes().toArray(new Node[]{});
        Arrays.sort(nodes, Comparator.comparingInt(Node::getID));

        if (tp.isDirected())
            return encodeDigraph6(tp, nodes, withHeader);
        return encodeGraph6(tp, nodes, withHeader);
    }

    /**
     * Parse the input string {@code data} and determines file format according
     * to its header of its first byte.
     */
    public static void decodeGraph(Topology tp, String data) {
        String[] lines = data.split("\n");
        if (lines.length == 0)
            return;
        if (lines.length > 1 && lines[1].length() > 0) {
            System.err.println("warning: reading a file with several graphs; " +
                    "only the first one will be loaded.");
        }
        String input = lines[0];

        if (input.startsWith(DIGRAPH6_HEADER) || input.startsWith(DIGRAPH6_STARTER))
            decodeGraph(tp, DIGRAPH6_HEADER, DIGRAPH6_STARTER,
                    (nodes, edges) -> decodeDigraph6Edges(tp, nodes, edges),
                    input);
        else if (input.startsWith(SPARSE6_HEADER) ||
                input.startsWith(SPARSE6_INCREMENTAL_STARTER) ||
                input.startsWith(SPARSE6_STARTER))
            throw new Graph6Exception("sparse6 format is not yet supported.");
        else {
            decodeGraph(tp, GRAPH6_HEADER, GRAPH6_STARTER,
                    (nodes, edges) -> decodeGraph6Edges(tp, nodes, edges),
                    input);
        }
    }

    public static String removeHeader(String data) {
        if (data.startsWith(GRAPH6_HEADER))
            return data.substring(GRAPH6_HEADER.length());
        if (data.startsWith(DIGRAPH6_HEADER))
            return data.substring(DIGRAPH6_HEADER.length());
        if (data.startsWith(SPARSE6_HEADER))
            return data.substring(SPARSE6_HEADER.length());
        return data;
    }

    public static boolean startsWithHeader(String data) {
        return (data.startsWith(GRAPH6_HEADER) ||
                data.startsWith(DIGRAPH6_HEADER) ||
                data.startsWith(SPARSE6_HEADER));
    }
    // endregion API to package

    // region private helper function
    private static String encodeGraph6(Topology tp, Node[] sortedNodes,
                                       boolean withHeader) {
        int nbnodes = sortedNodes.length;
        int nbbits = nbnodes * (nbnodes - 1) / 2;
        BitVector bv = new BitVector(nbbits);
        int bitindex = 0;
        for (int j = 1; j < sortedNodes.length; j++) {
            for (int i = 0; i < j; i++) {
                if (tp.getLink(sortedNodes[i], sortedNodes[j]) != null)
                    bv.set(bitindex, true);
                bitindex++;
            }
        }
        assert (bv.getNbBits() == nbbits);

        return graphToString(withHeader, GRAPH6_HEADER, GRAPH6_STARTER, nbnodes,
                bv);
    }

    private static void decodeGraph6Edges(Topology tp, Node[] nodes,
                                          BitVector edges) {
        int missingBits = (nodes.length * (nodes.length - 1) / 2)
                - edges.getNbBits();
        if (missingBits > 0)
            throw new Graph6Exception("input bitvector misses " + missingBits +
                    "bits");

        tp.setOrientation(Link.Orientation.UNDIRECTED);
        int bitindex = 0;
        for (int j = 1; j < nodes.length; j++) {
            for (int i = 0; i < j; i++) {
                if (edges.get(bitindex))
                    tp.addLink(new Link(nodes[i], nodes[j]),
                            true);
                bitindex++;
            }
        }
    }

    private static void decodeDigraph6Edges(Topology tp, Node[] nodes,
                                            BitVector edges) {
        int missingBits = nodes.length * nodes.length - edges.getNbBits();
        if (missingBits > 0)
            throw new Graph6Exception("input bitvector misses " + missingBits +
                    "bits");

        tp.setOrientation(Link.Orientation.DIRECTED);
        int bitindex = 0;
        for (Node src : nodes) {
            for (Node tgt : nodes) {
                if (edges.get(bitindex)) {
                    Link l = new Link(src, tgt, Link.Orientation.DIRECTED);
                    tp.addLink(l, true);
                }
                bitindex++;
            }
        }

    }

    private static String encodeDigraph6(Topology tp, Node[] sortedNodes,
                                         boolean withHeader) {
        int nbnodes = sortedNodes.length;
        int nbbits = nbnodes * nbnodes;
        BitVector bv = new BitVector(nbbits);
        int bitindex = 0;
        for (Node src : sortedNodes) {
            for (Node tgt : sortedNodes) {
                if (tp.getLink(src, tgt, Link.Orientation.DIRECTED) != null)
                    bv.set(bitindex, true);
                bitindex++;
            }
        }
        assert (bv.getNbBits() == nbbits);

        return graphToString(withHeader, DIGRAPH6_HEADER, DIGRAPH6_STARTER,
                nbnodes, bv);
    }

    private static String graphToString(boolean withHeader, String header,
                                        String starter, long nbNodes,
                                        BitVector edges) {
        StringBuilder result = new StringBuilder();
        if (withHeader)
            result.append(header);
        result.append(starter);
        result.append(new String(Graph6BVHelper.N(nbNodes)));
        result.append(new String(Graph6BVHelper.R(edges)));

        return result.toString();
    }

    private static void decodeGraph(Topology tp, String header, String starter,
                                    BiConsumer<Node[], BitVector> edgeBuilder,
                                    String input) {
        int offset;
        if (input.startsWith(">>")) {
            if (!input.startsWith(header)) {
                String wrongheader;
                if (input.contains("<<")) {
                    wrongheader = input.substring(0, input.indexOf("<<") + 2);
                } else {
                    wrongheader = input.substring(0, header.length());
                }
                throw new Graph6Exception("invalid header '" + wrongheader +
                        "' while " + header + " is expected.");
            }
            offset = header.length();
        } else
            offset = 0;

        if (!starter.equals(input.substring(offset, offset + starter.length()))) {
            throw new Graph6Exception("invalid prefix for format (" + header +
                    "); '" + starter + "' was expected.");
        } else {
            offset += starter.length();
        }

        byte[] in = input.getBytes();
        Graph6BVHelper.ReadLongResult res = Graph6BVHelper.readN(in, offset);
        BitVector bv = Graph6BVHelper.readR(in, offset + res.nbBytesRead);
        Node[] nodes = new Node[(int) res.value];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
            nodes[i].setID(i);
            tp.addNode(nodes[i]);
        }
        edgeBuilder.accept(nodes, bv);
    }
    // endregion private helper function
}
