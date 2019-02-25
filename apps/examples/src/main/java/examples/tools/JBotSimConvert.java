package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.TopologySerializerFilenameMatcher;
import io.jbotsim.io.format.tikz.TikzTopologySerializer;
import io.jbotsim.io.format.TopologySerializer;
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
        String exportedTopology = serializer.exportTopology(tp);

        if(exportedTopology != null)
            tp.getFileManager().write(filename, exportedTopology);
    }

    private static void importTopology(String filename, Topology tp) {
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
