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
import io.jbotsim.io.format.TopologySerializerFilenameMatcher;

/**
 * This class implements support for Graph6 file format as described in
 * <a href="https://users.cecs.anu.edu.au/~bdm/data/formats.txt">https://users.cecs.anu.edu.au/~bdm/data/formats.txt</a>.
 * <p>
 * Only Graph6 and Digraph6 format are supported. Sparse6 format is not yet
 * implemented.
 */
public class Graph6TopologySerializer implements TopologySerializer {
    private static final boolean DEFAULT_GENERATE_HEADERS = true;
    private boolean generateHeaders = DEFAULT_GENERATE_HEADERS;

    /**
     * Supported filename extensions. Can be used to populate a
     * {@link TopologySerializerFilenameMatcher}.
     * @see TopologySerializerFilenameMatcher#addTopologySerializer(String, TopologySerializer) 
     */
    public static final String[] GRAPH6_FILENAME_EXTENSIONS = new String[] {
        "g6", "d6"
    };

    /**
     * Encode the graph of {@code topology} in Graph6 or Digraph6 file format
     * according to {@link Topology#isDirected()}.
     *
     * @param topology The {@link Topology} object which must be exported
     * @return the Graph6 encoding of this topology
     */
    @Override
    public String exportToString(Topology topology) {
        return Graph6Codec.encodeGraph(topology, generateHeaders);
    }

    /**
     * Populate {@code topology} with the graph described in {@code data}.
     * The orientation of {@code topology} might be adjusted wrt to the
     * orientation of the graph in {@code data}.
     *
     * @param topology The {@link Topology} object which must be populated
     */
    @Override
    public void importFromString(Topology topology, String data) {
        Graph6Codec.decodeGraph(topology, data);
    }

    /**
     * @return if this serializer produces Graph6 header in outputs
     */
    public boolean generatesHeaders() {
        return generateHeaders;
    }

    /**
     *
     * @param generateHeaders is true if this serializer has to generate Graph6
     *                        headers.
     */
    public void setGenerateHeaders(boolean generateHeaders) {
        this.generateHeaders = generateHeaders;
    }
}
