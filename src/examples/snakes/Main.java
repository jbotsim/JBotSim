package examples.snakes;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.topology.TopologyGenerator;
import jbotsimx.misc.UtilClock;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology(1280, 720);
        //tp.setDefaultNodeModel(Snake.class);
        //
        deployNodes(tp); // optional
        Snake[] snakes = new Snake[3];
        for (Snake s : snakes) {
            s = new Snake(tp, 5);
            s.setClockModel(new UtilClock(tp.clockManager).getClass());
            s.setClockSpeed(500);
            s.start();
        }
        new JViewer(tp);

    }

    // Deploy a few nodes to save the user some time
    private static void deployNodes(Topology tp) {
        TopologyGenerator.generateTriangleGrid(tp, 30,23);
    }

}