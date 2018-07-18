package examples.replay;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.format.xml.XMLTraceParser;
import jbotsimx.replay.TracePlayer;

public class Replay {
    public static void main(String[] args) {
        try {
            TracePlayer tp = new TracePlayer(new Topology());
            XMLTraceParser parser = new XMLTraceParser(tp);
            parser.parse("trace.xml");
            new JViewer(tp.getTopology());
            tp.getTopology().start();
            tp.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
