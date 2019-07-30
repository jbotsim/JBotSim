package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.TopologySerializerFilenameMatcher;
import io.jbotsim.io.format.graph6.Graph6TopologySerializer;
import io.jbotsim.io.format.tikz.TikzTopologySerializer;
import io.jbotsim.io.TopologySerializer;
import io.jbotsim.io.format.dot.DotTopologySerializer;
import io.jbotsim.io.format.plain.PlainTopologySerializer;
import io.jbotsim.io.format.xml.XMLTopologySerializer;
import io.jbotsim.ui.JViewer;

public class JBotSimViewer {
    public static void main(String[] args) {
        Topology tp = new Topology();
        if (args.length > 0) {
            String filename = args[0];
            importTopology(tp, filename);
        }
        new JViewer(tp);
        tp.start();
    }

    private static void importTopology(Topology tp, String filename) {
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
        filenameMatcher.addTopologySerializer(".*\\.g6$",new Graph6TopologySerializer());
        filenameMatcher.addTopologySerializer(Graph6TopologySerializer.GRAPH6_FILENAME_EXTENSIONS,
                                              new Graph6TopologySerializer());

        return filenameMatcher;
    }
}
