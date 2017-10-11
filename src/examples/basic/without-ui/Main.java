package examples.basic.withoutui;

import jbotsim.Topology;
import jbotsim.ui.JViewer;
import jbotsimx.Connectivity;

/**
 * Created by acasteig on 25/08/15.
 */
public class Main {

    static final int nodeCount = 100;

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(BroadcastingNode.class);
        BroadcastingNode.nodeCount = nodeCount;

        tp.addStartListener(() -> {
            System.out.println("Start");
            do {
              tp.clear();
              deployNodes(tp);
            } while(!Connectivity.isConnected(tp));
            BroadcastingNode.informedNodeCount = 0;
            tp.getNodes().get(0).onSelection();
        });

        tp.restart();
        tp.runForever();
    }

    // Deploy a few nodes to save the user some time
    private static void deployNodes(Topology tp) {
        for (int i = 0; i < nodeCount; i++){
            tp.addNode(-1, -1);
        }
    }
}
