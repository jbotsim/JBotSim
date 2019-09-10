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

package io.jbotsim.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.jbotsim.core.Link.Orientation.DIRECTED;
import static io.jbotsim.core.Link.Orientation.UNDIRECTED;
import static org.junit.jupiter.api.Assertions.*;

public class OrientationTest {
    private static final Link.Orientation[] ORIENTATIONS = {UNDIRECTED, DIRECTED};
    private static final int[][] SQUARE_TOPOLOGY = {{0, 1}, {1, 2}, {2, 3}, {3, 0}};
    private static final int[][] TETRAHEDRON_TOPOLOGY = {
            {0, 1}, {1, 2}, {2, 0},
            {0, 3}, {1, 3}, {2, 3}};
    private static final int[][] TRIANGLE_TOPOLOGY = {{0, 1}, {1, 2}, {2, 0}};
    private static final int[][] EMPTY_TOPOLOGY = {};
    private static final int[][] ONE_ARC_TOPOLOGY = {{0, 1}};
    private static final int[][] LOOP_TOPOLOGY = {{0, 0}, {0,1}, {1,1}};

    static Stream<Arguments> TOPOLOGIES_WITH_LOOPS () {
        return Stream.of(Arguments.of((Object) LOOP_TOPOLOGY));
    }

    static Stream<Arguments> TOPOLOGIES_NO_LOOP() {
        return Stream.of(
                Arguments.of((Object) ONE_ARC_TOPOLOGY),
                Arguments.of((Object) TRIANGLE_TOPOLOGY),
                Arguments.of((Object) SQUARE_TOPOLOGY),
                Arguments.of((Object) TETRAHEDRON_TOPOLOGY)
        );
    }

    static Stream<Arguments> ALL_TOPOLOGIES() {
        return Stream.of(TOPOLOGIES_NO_LOOP(),
                         TOPOLOGIES_WITH_LOOPS(),
                         Stream.of(Arguments.of((Object) EMPTY_TOPOLOGY))).flatMap(i->i);
    }

    private static Node findOrAddNode(Topology topology, int id) {
        Node result = topology.findNodeById(id);
        if (result == null) {
            result = new Node();
            result.setID(id);
            topology.addNode(result);
        }
        return result;
    }

    /*
     * Create a topology from the specified array of 'edges'. The orientation of
     * the result is enforced to 'defaultOrientation' and links are created using
     * 'linksOrientation' orientation.
     * In order to prevent implicit creation of links, wireless is disabled.
     */
    private static Topology buildTopology(int[][] edges,
                                          Link.Orientation defaultOrientation,
                                          Link.Orientation linksOrientation) {
        Topology result = new Topology();
        result.disableWireless();
        result.setOrientation(defaultOrientation);
        for (int[] e : edges) {
            Node src = findOrAddNode(result, e[0]);
            Node tgt = findOrAddNode(result, e[1]);
            Link l = new Link(src, tgt, linksOrientation);
            result.addLink(l, true);
        }

        return result;
    }

    /*
     * Create a topology from the specified array of 'edges'. The orientation of
     * the result is enforced to 'defaultOrientation'. Links are created as
     * links are specified as directed and in both directions i.e {x,y} yields
     * the creation of links x -> y and y -> x.
     * In order to prevent implicit creation of links, wireless is disabled.
     */
    private static Topology buildBothWaysEdgesTopology(int[][] edges,
                                                       Link.Orientation defaultOrientation) {
        Topology result = new Topology();
        result.disableWireless();
        result.setOrientation(defaultOrientation);
        for (int[] e : edges) {
            Node src = findOrAddNode(result, e[0]);
            Node tgt = findOrAddNode(result, e[1]);
            result.addLink(new Link(src, tgt, DIRECTED), true);
            if (e[0] != e[1])
                result.addLink(new Link(tgt, src, DIRECTED), true);
        }

        return result;
    }

    @Test
    void checkDefaultOrientation() {
        assertEquals(Topology.DEFAULT_ORIENTATION, new Topology().getOrientation());
    }

    @ParameterizedTest
    @MethodSource("ALL_TOPOLOGIES")
    @SuppressWarnings("deprecation")
    void checkSimpleUndirectedTopology(int[][] edges) {
        Topology tp = buildTopology(edges, UNDIRECTED, UNDIRECTED);
        assertFalse(tp.hasDirectedLinks());
        assertFalse(tp.isDirected());

        List<Link> links = tp.getLinks();
        assertEquals(edges.length, links.size());

        links = tp.getLinks(UNDIRECTED);
        assertEquals(edges.length, links.size());

        links = tp.getLinks(DIRECTED);
        assertEquals(2 * edges.length, links.size());
    }

    private int countLoops(Topology tp) {
        int result = 0;
        for (Link l : tp.getLinks(UNDIRECTED)) {
            if (l.source.equals(l.destination))
                result++;
        }
        return result;
    }

    private boolean hasLoops(Topology tp) {
        return countLoops(tp) > 0;
    }

    @ParameterizedTest
    @MethodSource("TOPOLOGIES_NO_LOOP")
    @SuppressWarnings("deprecation")
    void checkSimpleDirectedTopology(int[][] edges) {
        Topology tp = buildTopology(edges, DIRECTED, DIRECTED);
        assertTrue(tp.isDirected());
        assertTrue(tp.hasDirectedLinks());

        List<Link> links = tp.getLinks();
        assertEquals(edges.length, links.size());

        links = tp.getLinks(UNDIRECTED);
        assertTrue(links.isEmpty());

        links = tp.getLinks(DIRECTED);
        assertEquals(edges.length, links.size());
    }

    @Test
    void checkSimpleDirectedEmptyTopology() {
        Topology tp = buildTopology(EMPTY_TOPOLOGY, DIRECTED, DIRECTED);
        assertTrue(tp.isDirected());
        assertTrue(EMPTY_TOPOLOGY.length == 0);

        assertTrue(tp.getLinks().isEmpty());
        assertTrue(tp.getLinks(UNDIRECTED).isEmpty());
        assertTrue(tp.getLinks(DIRECTED).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("TOPOLOGIES_WITH_LOOPS")
    void checkSimpleDirectedTopologyWithLoops(int[][] edges) {
        Topology tp = buildTopology(edges, DIRECTED, DIRECTED);
        assertTrue(tp.isDirected());
        assertTrue(hasLoops(tp));

        List<Link> links = tp.getLinks();
        assertEquals(edges.length, links.size());

        links = tp.getLinks(DIRECTED);
        assertEquals(edges.length, links.size());
    }

    @ParameterizedTest
    @MethodSource("ALL_TOPOLOGIES")
    @SuppressWarnings("deprecation")
    void checkMixedDirectedUndirectedTopology(int[][] edges) {
        Topology tp = buildTopology(edges, DIRECTED, UNDIRECTED);
        assertTrue(tp.isDirected());
        assertFalse(tp.hasDirectedLinks());

        List<Link> links = tp.getLinks();
        assertEquals(2 * edges.length, links.size());

        links = tp.getLinks(UNDIRECTED);
        assertEquals(edges.length, links.size());

        links = tp.getLinks(DIRECTED);
        assertEquals(2 * edges.length, links.size());
    }

    @ParameterizedTest
    @MethodSource("TOPOLOGIES_NO_LOOP")
    @SuppressWarnings("deprecation")
    void checkMixedUndirectedDirectedTopology(int[][] edges) {
        Topology tp = buildTopology(edges, UNDIRECTED, DIRECTED);
        assertFalse(tp.isDirected());
        assertTrue(tp.hasDirectedLinks());

        List<Link> links = tp.getLinks();
        assertTrue(links.isEmpty());

        links = tp.getLinks(UNDIRECTED);
        assertTrue(links.isEmpty());

        links = tp.getLinks(DIRECTED);
        assertEquals(edges.length, links.size());
    }

    @ParameterizedTest
    @MethodSource("TOPOLOGIES_WITH_LOOPS")
    void checkMixedUndirectedDirectedTopologyWithLoops(int[][] edges) {
        Topology tp = buildTopology(edges, UNDIRECTED, DIRECTED);
        assertFalse(tp.isDirected());
        assertTrue(hasLoops(tp));

        List<Link> links = tp.getLinks(DIRECTED);
        assertEquals(edges.length, links.size());
    }

    @Test
    void checkMixedUndirectedDirectedEmptyTopology() {
        Topology tp = buildTopology(EMPTY_TOPOLOGY, UNDIRECTED, DIRECTED);
        assertFalse(tp.isDirected());
        assertTrue(EMPTY_TOPOLOGY.length == 0);

        assertTrue(tp.getLinks().isEmpty());
        assertTrue(tp.getLinks(UNDIRECTED).isEmpty());
        assertTrue(tp.getLinks(DIRECTED).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("ALL_TOPOLOGIES")
    @SuppressWarnings("deprecation")
    void checkBothWaysEdgesUndirectedTopology(int[][] edges) {
        Topology tp = buildBothWaysEdgesTopology(edges, UNDIRECTED);
        assertFalse(tp.isDirected());
        assertFalse(tp.hasDirectedLinks());

        List<Link> links = tp.getLinks();
        assertEquals(edges.length, links.size());

        links = tp.getLinks(UNDIRECTED);
        assertEquals(edges.length, links.size());

        links = tp.getLinks(DIRECTED);
        assertEquals(2 * edges.length-countLoops(tp), links.size());
    }

    @ParameterizedTest
    @MethodSource("ALL_TOPOLOGIES")
    void checkEnforcedLinksOrientation(int[][] edges) {
        for (Link.Orientation to : ORIENTATIONS) {
            for (Link.Orientation lo : ORIENTATIONS) {
                Topology tp1 = buildTopology(edges, to, lo);
                checkLinksOrientation(tp1);
                Topology tp2 = buildBothWaysEdgesTopology(edges, to);
                checkLinksOrientation(tp2);
            }
        }
    }

    void checkLinksOrientation(Topology tp) {
        for (Link l : tp.getLinks()) {
            assertEquals(tp.getOrientation(), l.orientation);
            assertEquals(l.isDirected(), l.orientation == DIRECTED);
        }

        for (Link.Orientation to : ORIENTATIONS) {
            for (Link l : tp.getLinks(to)) {
                assertEquals(to, l.orientation);
                assertEquals(l.isDirected(), l.orientation == DIRECTED);
            }
        }
    }
}
