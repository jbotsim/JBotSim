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
package io.jbotsim.io.format.xml;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyGenerators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BuilderParserTest {
    interface TopologyMaker {
        Topology make() throws XMLTopologyParser.ParserException;
    }

    @Before
    public void setUp() {
        rnd.setSeed(0x13456789);
    }

    @Parameterized.Parameter
    public TopologyMaker maker;

    @Test
    public void builderParserTest() throws XMLTopologyParser.ParserException, XMLTopologyBuilder.BuilderException {
        runBuilderParserTest(maker);
    }

    public static void runBuilderParserTest(TopologyMaker mk)
            throws XMLTopologyParser.ParserException, XMLTopologyBuilder.BuilderException {
        Topology T1 = mk.make();
        String t1 = new XMLTopologyBuilder(T1).writeToString();

        Topology T2 = new Topology();
        XMLTopologyParser tpp = new XMLTopologyParser(T2, true);
        tpp.parseFromString(t1);

        String t2 = new XMLTopologyBuilder(T2).writeToString();

        assertEquals(t1, t2);
    }

    @Parameters(name="{index}: {0}")
    public static Collection<TopologyMaker> makers() {
        return Arrays.asList(
                BuilderParserTest::T1,
                () -> T2(true),
                () -> T2(false)
        );
    }

    public static class TestNode1 extends Node { }
    public static class TestNode2 extends Node { }
    public static class TestNode3 extends Node { }

    private static final Random rnd = new Random(0);

    private static Node getRandomNode () {
        switch (rnd.nextInt(3)){
            case 0: return new TestNode1();
            case 1: return new TestNode2();
            case 2: return new TestNode3();
        }
        return new Node();
    }

    private static Topology T1() {
        Topology T = new Topology();
        T.setNodeModel("test1", TestNode1.class);
        T.setNodeModel("test2", TestNode2.class);
        T.setDefaultNodeModel(TestNode3.class);
        TopologyGenerators.generateRing(T, 5, true);
        T.getNodes().get(2).setColor(Color.red);

        return T;
    }

    private static Topology T2(boolean enableWireless) {
        Topology T = new Topology();
        T.setNodeModel("test1", TestNode1.class);
        T.setNodeModel("test2", TestNode2.class);
        T.setDefaultNodeModel(TestNode3.class);
        int nbNodes = 5 + rnd.nextInt(15);

        if (enableWireless)
            T.enableWireless();
        else
            T.disableWireless();

        for (int i = 0; i < nbNodes; i++) {
            Node n = getRandomNode();
            T.addNode(-1, -1, n);
            n.setColor(Color.getRandomColor());
        }
        if (!enableWireless) {
            List<Node> nodes = T.getNodes();
            for (int i = 0; i < nbNodes; i++) {
                Node src = nodes.get(i);
                for (int j = i + 1; j < nbNodes; j++) {
                    if (rnd.nextDouble() > 0.5)
                        continue;
                    Node dst = nodes.get(j);
                    Link l = new Link(src, dst);
                    l.setColor(new Color(rnd.nextInt(0xFFFFFF)));
                    T.addLink(l, true);
                }
            }
        }

        return T;
    }
}
