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

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DotImportExportCoherencyTest {
    private static final int TEST_SEED = 314156;
    private static final int MAX_DEGREE = 5;
    private static final int MAX_NUMBER_OF_NODES = 100;
    private static final int NB_IDEMPOTENCE_TESTS = 200;

    @Parameterized.Parameter
    public int prngSeeds;

    @Parameterized.Parameters(name = "{index}: seed={0}")
    public static Collection<Integer> makers() {
        Random rnd = new Random(TEST_SEED);
        ArrayList<Integer> res = new ArrayList<>(NB_IDEMPOTENCE_TESTS);
        res.add(rnd.nextInt());
        for (int i = 1; i < NB_IDEMPOTENCE_TESTS; i++) {
            int prev = res.get(i - 1);
            if (prev % 2 == 0) {
                res.add((rnd.nextInt() + prev) / 2);
            } else {
                res.add(3 * (rnd.nextInt() + prev) + 1);
            }
        }
        return res;
    }

    public static class TestNode1 extends Node {
    }

    public static class TestNode2 extends Node {
    }

    private Node buildRandomNode(Random rnd, Topology tp) throws IllegalAccessException, InstantiationException {
        Class<? extends Node>[] templates = new Class[]{TestNode1.class, TestNode2.class, Node.class};
        int c = rnd.nextInt(templates.length);
        Node result = templates[c].newInstance();
        result.setLocation(rnd.nextInt(tp.getWidth()), rnd.nextInt(tp.getHeight()));

        return result;
    }

    private Topology buildRandomTopology(int nbNodes, Random rnd) throws InstantiationException, IllegalAccessException {
        Topology tp = new Topology();
        for (int i = 0; i < nbNodes; i++)
            tp.addNode(buildRandomNode(rnd, tp));

        Node[] nodes = tp.getNodes().toArray(new Node[0]);

        if (nbNodes == 1) {
            tp.getLink(nodes[0], nodes[0], rnd.nextBoolean());
        } else {
            for (int src = 0; src < nbNodes; src++) {
                Node srcNode = nodes[src];
                int nbEdges = rnd.nextInt(MAX_DEGREE);
                while (nbEdges-- > 0) {
                    Node dstNode;
                    do {
                        dstNode = nodes[rnd.nextInt(nbNodes)];
                    } while (srcNode == dstNode);


                    if (srcNode.getCommonLinkWith(dstNode) == null && srcNode.getOutLinkTo(dstNode) == null &&
                            dstNode.getOutLinkTo(srcNode) == null) {
                        Link.Type type = rnd.nextBoolean() ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
                        tp.addLink(new Link(srcNode, dstNode, type));
                    } else if (dstNode.getOutLinkTo(dstNode) != null) {
                        tp.addLink(new Link(srcNode, dstNode, Link.Type.DIRECTED));
                    }
                }
            }
        }

        return tp;
    }

    public void checkTopologyGraph(Topology tp1, Topology tp2) {
        assertEquals(tp1.getNodes().size(), tp2.getNodes().size());
        assertEquals(tp1.getLinks(true).size(), tp2.getLinks(true).size());
        assertEquals(tp1.getLinks(false).size(), tp2.getLinks(false).size());

        HashMap<Integer, Node> nodes1 = new HashMap<>();
        for (Node n : tp1.getNodes()) {
            nodes1.put(n.getID(), n);
        }
        HashMap<Integer, Node> nodes2 = new HashMap<>();
        for (Node n : tp2.getNodes()) {
            nodes2.put(n.getID(), n);
        }
        assertEquals(nodes1.keySet(), nodes2.keySet());
        for (Node src1 : tp1.getNodes()) {
            Node src2 = nodes2.get(src1.getID());
            Set<Integer> neighbours = new HashSet<>();
            for (Node n : src1.getNeighbors()) {
                neighbours.add(n.getID());
            }
            for (Node n : src2.getNeighbors()) {
                assertTrue(neighbours.contains(n.getID()));
            }
        }
    }

    public static String reorderLines(String s) {
        String[] lines = s.split("\n");
        Arrays.sort(lines);
        StringBuilder sb = new StringBuilder();
        for (String l : lines)
            sb.append(l).append('\n');
        return sb.toString();
    }

    private void runTest(int size, Random rnd) throws IllegalAccessException, InstantiationException {
        DotTopologySerializer dotIO = new DotTopologySerializer(false);
        Topology tp1 = buildRandomTopology(size, rnd);
        Topology tp2 = new Topology();

        String s1 = dotIO.exportToString(tp1);
        dotIO.importFromString(tp2, s1);
        checkTopologyGraph(tp1, tp2);
        String s2 = dotIO.exportToString(tp2);

        assertEquals(reorderLines(s1), reorderLines(s2));
    }

    @Test
    public void importExportCoherencyTest() throws IllegalAccessException, InstantiationException {
        Random rnd = new Random(prngSeeds);
        int maxSmallSize = 3;
        for (int i = 0; i <= maxSmallSize; i++)
            runTest(i, rnd);
        runTest(1 + maxSmallSize + rnd.nextInt(MAX_NUMBER_OF_NODES - maxSmallSize), rnd);
    }
}
