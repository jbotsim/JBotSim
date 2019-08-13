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

import static io.jbotsim.core.Link.Orientation;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests in this class check the consistency of the pipeline
 * encoding -> decoding -> re-encoding for simple graphs.
 */
class Graph6CodecTest {
    // region test function
    private static final int NB_NODES_MAX = 50;

    private static Stream<Arguments> ENUMERATE_NB_NODES() {
        return addTestParametersToStream(rangeStream(0, NB_NODES_MAX));
    }

    @ParameterizedTest
    @MethodSource("ENUMERATE_NB_NODES")
    void encodeDecodeNoEdges(int nbNodes, boolean withHeader,
                             Orientation orientation) {
        Topology tp_orig = buildTopologyWithoutEdges(nbNodes, orientation);
        assertEquals(tp_orig.getOrientation(), orientation);
        Topology tp_encoded = encodeDecode(tp_orig, withHeader);
        assertRoughlyEquals(tp_orig, tp_encoded);
    }

    private static Stream<Arguments> LINE_GRAPHS() {
        return addTestParametersToStream(rangeStream(2, NB_NODES_MAX));
    }

    @ParameterizedTest(name = "line with {0} nodes, header = {1}, orientation = {2}")
    @MethodSource("LINE_GRAPHS")
    void encodeDecodeLineGraph(int nbNodes, boolean withHeader,
                               Orientation orientation) {
        int[][] edges = buildLineGraph(nbNodes);
        checkGraphByEdges(edges, withHeader, orientation);
    }

    private static final int[][][] SIMPLE_GRAPHS = new int[][][]{
            {{0, 1}},
            {{0, 1}, {1, 2}, {2, 3}, {3, 4}},
            {{0, 2}, {0, 4}, {3, 1}, {3, 4}},
            {}
    };

    private static Stream<Arguments> GRAPHS_PARAMETERS() {
        return addTestParametersToStream(rangeStream(0, SIMPLE_GRAPHS.length - 1));
    }

    @ParameterizedTest(name = "graph {0}, header = {1}, orientation = {2}")
    @MethodSource("GRAPHS_PARAMETERS")
    void encodeDecodeSimpleGraphs(int graph_index, boolean withHeader,
                                  Orientation orientation) {
        checkGraphByEdges(SIMPLE_GRAPHS[graph_index], withHeader, orientation);
    }
    // endregion

    // region helper functions
    //   region stream of parameters

    /**
     * Generate the stream of integers min, min+1, ..., max-1, max
     *
     * @param min minimal bound of the range
     * @param max maximal bound of the range
     * @return the stream of integers min, min+1, ..., max-1, max
     */
    private static Stream<Integer> rangeStream(int min, int max) {
        return Stream.iterate(min, i -> i + 1).limit(max - min + 1);
    }


    /**
     * Transform a stream into a stream with some additional parameters needed
     * for tests:
     * - orientation: the default orientation of links used to create topologies
     * - header: a Boolean that indicates if headers are generated in Graph6 outputs.
     *
     * @param input the original stream
     * @return the stream extended with additional parameters
     */
    private static <T> Stream<Arguments> addTestParametersToStream(Stream<T> input) {
        return input.flatMap(t -> {
            Stream<Arguments> result = Stream.empty();
            for (Orientation orientation : Orientation.values()) {
                Stream<Arguments> tmp =
                        Stream.of(Arguments.of(t, true, orientation),
                                Arguments.of(t, false, orientation));
                result = Stream.concat(result, tmp);
            }
            return result;

        });
    }
    //  endregion stream of parameters

    //  region correctness checking
    /**
     * Encode a Topology in G6 format then decode it and re-encode the decoded
     * topology. Following assertions are checked:
     * - if an header is requested, then it is present.
     * - both generated encoding strings are equal.
     * - sets of directed/undirected links and sets of nodes have the same
     *   cardinality {@link #assertRoughlyEquals}.
     */
    private Topology encodeDecode(Topology tp_orig, boolean withHeader) {
        String g6str = Graph6Codec.encodeGraph(tp_orig, withHeader);
        assertEquals(withHeader, Graph6Codec.startsWithHeader(g6str));

        Topology tp_enc = new Topology();
        tp_enc.disableWireless();
        Graph6Codec.decodeGraph(tp_enc, g6str);
        assertRoughlyEquals(tp_orig, tp_enc);

        String g6str2 = Graph6Codec.encodeGraph(tp_enc, withHeader);

        assertEquals(g6str, g6str2);

        return tp_enc;
    }

    /**
     * Check if the given topologies are more or less "equals".
     *
     * @param expected the expected result
     * @param actual   the actually computed topology
     */
    static void assertRoughlyEquals(Topology expected, Topology actual) {
        assertEquals(expected.getOrientation(), actual.getOrientation());

        assertEquals(expected.getNodes().size(), expected.getNodes().size());
        for(Node n : expected.getNodes()) {
            assertNotNull(actual.findNodeById(n.getID()));
        }
        for(Node n : actual.getNodes()) {
            assertNotNull(expected.findNodeById(n.getID()));
        }
        List<Link> expected_edges = expected.getLinks();
        List<Link> actual_edges = actual.getLinks();

        assertEquals(expected_edges.size(), actual_edges.size());

        Orientation orientation = expected.getOrientation();
        for (Link l : expected_edges) {
            Node src = actual.findNodeById(l.source.getID());
            Node tgt = actual.findNodeById(l.destination.getID());

            assertNotNull(actual.getLink(src, tgt, orientation));
            if (orientation == Orientation.UNDIRECTED)
                assertNotNull(actual.getLink(tgt, src, Orientation.UNDIRECTED));
        }
    }

    /**
     * Check the consistency of the pipeline
     * encoding -> decoding -> re-encoding
     * for a topology build from the given array of {@code edges}.
     *
     * @param edges       the edges of the tested topology
     * @param withHeader  sepcifies if Graph6 header is generated
     * @param orientation the orientation used to create the topology
     */
    private void checkGraphByEdges(int[][] edges, boolean withHeader,
                                   Orientation orientation) {
        Topology tp = buildTopologyFromEdges(edges, orientation);
        assertEquals(tp.getOrientation(), orientation);
        Topology tp_enc = encodeDecode(tp, withHeader);
        assertRoughlyEquals(tp, tp_enc);
    }
    //  endregion correctness checking

    //  region builder functions

    /**
     * Check if a node exists in a topology w.r.t. its id and create it if the
     * node is not there.
     *
     * @param tp the input topology
     * @param id the looked up identifier of node
     * @return the node of tp with the identifier id
     */
    private Node findOrAddNode(Topology tp, int id) {
        Node result = tp.findNodeById(id);
        if (result == null) {
            result = new Node();
            result.setID(id);
            tp.addNode(result);
        }
        return result;
    }

    /**
     * Build a topology with the given orientation an {@code nbNodes} nodes.
     * Only nodes are created. No edge exists.
     *
     * @param nbNodes the expected number of nodes
     * @param orientation the default orientation of the topology.
     * @return a topology with the given 'orientation' and 'nbNodes' nodes.
     */
    private Topology buildTopologyWithoutEdges(int nbNodes,
                                               Orientation orientation) {
        Topology tp = new Topology();
        tp.disableWireless();
        tp.setOrientation(orientation);

        for (int i = 0; i < nbNodes; i++) {
            findOrAddNode(tp, i);
        }
        return tp;
    }

    /**
     * Build a topology from the given array of {@code edges} and a specified
     * {@code orientation}.
     *
     * @param edges the array of edges
     * @param orientation the default orientation of links
     * @return a topology from 'edges' and 'orientation'.
     */
    private Topology buildTopologyFromEdges(int[][] edges,
                                            Orientation orientation) {
        Topology tp = new Topology();
        tp.disableWireless();
        tp.setOrientation(orientation);

        for (int[] edge : edges) {
            Node src = findOrAddNode(tp, edge[0]);
            Node tgt = findOrAddNode(tp, edge[1]);
            tp.addLink(new Link(src, tgt, orientation), true);
        }

        return tp;
    }

    /**
     * Build an array of edges that forms a line of nodes.
     *
     * @param nbNodes the number of nodes in the line
     * @return an array  of couple of nodes
     */
    private static int[][] buildLineGraph(int nbNodes) {
        int[][] edges = new int[nbNodes - 1][];
        for (int s = 0; s < nbNodes - 1; s++) {
            edges[s] = new int[]{s, s + 1};
        }
        return edges;
    }
    //  endregion builder functions
    // endregion
}