package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.serialization.TopologySerializerFilenameMatcher;
import io.jbotsim.serialization.tikz.TikzTopologySerializer;
import io.jbotsim.serialization.TopologySerializer;
import io.jbotsim.serialization.dot.DotTopologySerializer;
import io.jbotsim.serialization.plain.PlainTopologySerializer;
import io.jbotsim.serialization.xml.XMLTopologySerializer;
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
        serializer.importTopology(tp, fileContent);
    }

    private static TopologySerializer getProperSerializer(String filename) {
        TopologySerializerFilenameMatcher matcher = getConfiguredTopologyFileNameMatcher();
        return matcher.getTopologySerializerFor(filename);
    }


    private static TopologySerializerFilenameMatcher getConfiguredTopologyFileNameMatcher() {
        TopologySerializerFilenameMatcher filenameMatcher = new TopologySerializerFilenameMatcher();
        filenameMatcher.addTopologySerializer(".*\\.dot$",new DotTopologySerializer());
        filenameMatcher.addTopologySerializer(".*\\.xdot$",new DotTopologySerializer());
        filenameMatcher.addTopologySerializer(".*\\.xml$",new XMLTopologySerializer(true));
        filenameMatcher.addTopologySerializer(".*\\.tikz$",new TikzTopologySerializer());
        filenameMatcher.addTopologySerializer(".*\\.plain$",new PlainTopologySerializer());
        return filenameMatcher;
    }
}
