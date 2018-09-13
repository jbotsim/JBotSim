package examples.features.replay;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.replay.TracePlayer;

public class Replay {
    public static void main(String[] args) {
        try {
            Topology topology = new Topology();
            new JViewer(topology);
            topology.start();

            TracePlayer tp = new TracePlayer(topology);
            tp.loadAndStart("trace.xml");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
