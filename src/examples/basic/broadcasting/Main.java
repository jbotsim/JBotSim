package examples.basic.broadcasting;

import jbotsim.Topology;
import jbotsim.ui.JViewer;

/**
 * Created by acasteig on 25/08/15.
 */
public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        JViewer viewer = new JViewer(tp);
        tp.setDefaultNodeModel(BroadcastingNode.class);
        viewer.setTimeUnit(500); // slow on purpose (500 ms per round)
        deployNodes(tp); // optional
    }

    // Deploy a few nodes to save the user some time
    private static void deployNodes(Topology tp) {
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 5; j++) {
                tp.addNode(50 + i * 80, 50 + j * 80);
            }
        }
    }
}
