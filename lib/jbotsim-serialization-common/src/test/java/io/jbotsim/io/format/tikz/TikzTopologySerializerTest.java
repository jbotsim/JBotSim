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

package io.jbotsim.io.format.tikz;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.io.TopologySerializer;
import io.jbotsim.io.format.dot.DotTopologySerializer;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TikzTopologySerializerTest {

    protected static final String RESULT_EXTENSION = "-res.tikz";

    private static final boolean UPDATE_RESULTS = false;
    private static final String TEST_RC_ROOT = "/tikzinputs/";


    @Parameterized.Parameters(name="{index}: {0}")
    public static Collection<String> classicalDotFiles() {
        return Arrays.asList(
                "syntax-04.xdot",
                "syntax-02.xdot",
                "syntax-01.gv",
                "syntax-02.gv",
                "syntax-03.gv",
                "syntax-01.xdot",
                "syntax-03.xdot",
                "hm-01.gv",
                "arboricity-100-2.gv",
                "barbell-6-5-4.gv",
                "cactus-20.gv",
                "kstar-20-2.gv",
                "gabriel-50.gv",
                "paley-10.gv",
                "paley-10.xdot",
                "sunlet-10-directed.gv");
    }

    private void updateExpectedResult(String result, String fileName) throws IOException {
        File resFile = new File(fileName + RESULT_EXTENSION);

        if(!resFile.exists())
            resFile.createNewFile();

        FileOutputStream out = new FileOutputStream(resFile);
        out.write(result.getBytes());
        out.flush();
        out.close();
    }

    private String loadExpectedResult(String fileName) throws IOException {
        URL url = getClass().getResource(TEST_RC_ROOT + fileName + RESULT_EXTENSION);
        return new String(Files.readAllBytes(Paths.get(url.getPath())));
    }

    private Topology importTestTopology(Topology topology, TopologySerializer serializer, String fileName) {
        URL url = getClass().getResource(TEST_RC_ROOT + fileName);

        String fileContent = topology.getFileManager().read(url.getPath());
        serializer.importFromString(topology, fileContent);
        assertNotNull(topology);
        return topology;
    }

    private void testExport(String fileName, Topology tp) throws IOException {
        String generatedTikz = new TikzTopologySerializer().exportToString(tp);
        assertNotNull(generatedTikz);

        if (UPDATE_RESULTS)
            updateExpectedResult(generatedTikz, fileName);

        String expectedTikz = loadExpectedResult(fileName);

        assertEquals(expectedTikz, generatedTikz);
    }

    @ParameterizedTest
    @MethodSource("classicalDotFiles")
    public void exportToString(String inputFileName) throws IOException {
        Topology tp = importTestTopology(new Topology(), new DotTopologySerializer(), inputFileName);

        testExport(inputFileName, tp);
    }

    @Test
    public void orientedCaseTest() throws IOException {
        Topology tp = new Topology();
        tp.setOrientation(Link.Orientation.DIRECTED);
        tp.disableWireless();
        tp.setSensingRange(30);
        Node n1 = new Node();
        n1.setColor(Color.RED);
        Node n2 = new Node();
        n2.setColor(new Color(25, 25,25));
        tp.addNode(50, 50, n1);
        tp.addNode(100, 100, n2);
        tp.addLink(new Link(n1, n2, tp.getOrientation(), Link.Mode.WIRED));


        testExport("oriented", tp);
    }

}