package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.gen.dynamic.trace.TracePlayer;
import io.jbotsim.io.format.xml.XMLTraceParser;
import io.jbotsim.ui.JViewer;

public class JBotSimPlayer {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: " + JBotSimPlayer.class.getName() + " input-file");
            System.exit(1);
        }
        String filename = args[0];

        try {
            Topology topology = new Topology();
            new JViewer(topology);
            TracePlayer tracePlayer = new TracePlayer(topology, new XMLTraceParser(topology, true));
            tracePlayer.loadAndStart(filename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
