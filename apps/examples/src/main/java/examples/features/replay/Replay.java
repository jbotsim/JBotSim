package examples.features.replay;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.replay.TracePlayer;

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
