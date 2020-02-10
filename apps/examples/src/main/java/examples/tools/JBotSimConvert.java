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

package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.TopologySerializerFilenameMatcher;
import io.jbotsim.io.format.graph6.Graph6TopologySerializer;
import io.jbotsim.io.format.tikz.TikzTopologySerializer;
import io.jbotsim.io.TopologySerializer;
import io.jbotsim.io.format.dot.DotTopologySerializer;
import io.jbotsim.io.format.plain.PlainTopologySerializer;
import io.jbotsim.io.format.xml.XMLTopologySerializer;

public class JBotSimConvert {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("usage: " + JBotSimConvert.class.getName() + " input-file output-file");
            System.exit(1);
        }
        String inputFileName = args[0];
        String outputFileName = args[1];

        Topology tp = new Topology();

        importTopology(inputFileName, tp);

        exportTopology(outputFileName, tp);

        System.exit(0);
    }

    private static void exportTopology(String filename, Topology tp) {
        TopologySerializer serializer = getProperSerializer(filename);
        String exportedTopology = serializer.exportToString(tp);

        if(exportedTopology != null)
            tp.getFileManager().write(filename, exportedTopology);
    }

    private static void importTopology(String filename, Topology tp) {
        String fileContent = tp.getFileManager().read(filename);

        TopologySerializer serializer = getProperSerializer(filename);
        serializer.importFromString(tp, fileContent);
    }

    private static TopologySerializer getProperSerializer(String filename) {
        TopologySerializerFilenameMatcher matcher = getConfiguredTopologyFileNameMatcher();
        return matcher.getTopologySerializerFor(filename);
    }


    private static TopologySerializerFilenameMatcher getConfiguredTopologyFileNameMatcher() {
        TopologySerializerFilenameMatcher filenameMatcher = new TopologySerializerFilenameMatcher();
        filenameMatcher.addTopologySerializer(DotTopologySerializer.DOT_FILENAME_EXTENSIONS,
                                              new DotTopologySerializer());
        filenameMatcher.addTopologySerializer(".*\\.xml$",new XMLTopologySerializer(true));
        filenameMatcher.addTopologySerializer(".*\\.tikz$",new TikzTopologySerializer());
        filenameMatcher.addTopologySerializer(".*\\.plain$",new PlainTopologySerializer());
        filenameMatcher.addTopologySerializer(Graph6TopologySerializer.GRAPH6_FILENAME_EXTENSIONS,
                                              new Graph6TopologySerializer());
        return filenameMatcher;
    }

}
