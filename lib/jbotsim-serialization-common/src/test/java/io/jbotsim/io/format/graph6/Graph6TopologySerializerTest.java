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

import io.jbotsim.core.Topology;
import io.jbotsim.io.TopologySerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class gather two tests apply to a set of existing Graph6 inputs:
 * 1 - the pipeline decoding/encoding is consistent.
 * 2 - the encoding with an header is just a prefix added to the encoding
 *     without header.
 *
 * Inputs graphs stored in resource files is a sample of graphs extracted from
 * Brendan McKay's webpage
 * <a href="https://users.cecs.anu.edu.au/~bdm/data/">Combinatorial Data</a>.
 */
class Graph6TopologySerializerTest {
    private static final String TEST_RC_ROOT = "/graph6inputs/";
    private static final String[] INPUT_FILES = {
            "graphs.g6",  "graphs-with-header.g6",
            "digraphs.d6", "digraphs-with-header.d6"
    };

    private static List<String> ENCODED_GRAPHS;

    @BeforeAll
    static void setUp() throws IOException {
        ENCODED_GRAPHS = loadTestFiles();
    }

    // region test functions
    private static Stream<String> ENCODED_GRAPHS_PARAMETERS() {
        return ENCODED_GRAPHS.stream();
    }

    @ParameterizedTest
    @MethodSource("ENCODED_GRAPHS_PARAMETERS")
    void checkDecodeEncodeGraphs(String inputGraph) {
        Graph6TopologySerializer ts = new Graph6TopologySerializer();
        Topology tp = importTopology(ts, inputGraph);

        ts.setGenerateHeaders(Graph6Codec.startsWithHeader(inputGraph));
        String g6str = ts.exportToString(tp);

        assertEquals(inputGraph, g6str);
    }

    @ParameterizedTest
    @MethodSource("ENCODED_GRAPHS_PARAMETERS")
    void checkDecodeEncodeChangeHeaderGraphs(String inputGraph) {
        Graph6TopologySerializer ts = new Graph6TopologySerializer();
        Topology tp = importTopology(ts, inputGraph);

        ts.setGenerateHeaders(true);
        String g6str_h = ts.exportToString(tp);

        ts.setGenerateHeaders(false);
        String g6str_no_h = ts.exportToString(tp);

        assertEquals(g6str_no_h, Graph6Codec.removeHeader(g6str_h));
    }
    // endregion test functions

    // region helper functions
    private static List<String> loadTestFiles() throws IOException {
        List<String> result = new ArrayList<>();
        for (String file : INPUT_FILES)
            loadTestFile(TEST_RC_ROOT + file, result);
        return result;
    }

    private static void loadTestFile(String inputFile, List<String> graphList) throws IOException {
        InputStream is = Graph6TopologySerializerTest.class.getResourceAsStream(inputFile);
        InputStreamReader isr = new InputStreamReader(is);
        LineNumberReader lnr = new LineNumberReader(isr);
        String line;
        while ((line = lnr.readLine()) != null) {
            if (!line.isEmpty())
                graphList.add(line);
        }
        isr.close();
    }

    private Topology importTopology(TopologySerializer ts, String strGraph) {
        Topology result = new Topology();
        result.disableWireless();
        ts.importFromString(result, strGraph);

        return result;
    }

    // endregion helper functions
}