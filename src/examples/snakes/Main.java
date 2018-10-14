package examples.snakes;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.topology.TopologyGenerator;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology(1280, 720);
        //tp.setDefaultNodeModel(Node.class);
        //tp.setClockSpeed(1000); // slow on purpose (500 ms per round)
        deployNodes(tp); // optional
        new JViewer(tp);
    }

    // Deploy a few nodes to save the user some time
    private static void deployNodes(Topology tp) {
        TopologyGenerator.generateTriangleGrid(tp, 30,23);
    }
}