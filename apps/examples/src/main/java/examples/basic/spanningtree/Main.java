package examples.basic.spanningtree;

import io.jbotsim.Link;
import io.jbotsim.Topology;
import io.jbotsim.event.StartListener;
import io.jbotsim.ui.JViewer;

/**
 * Created by acasteig on 25/08/15.
 */
public class Main {
    public static void main(String[] args) {
        final Topology tp = new Topology();
        tp.setDefaultNodeModel(SpanningTreeNode.class);
        tp.addStartListener(new StartListener() { // optional
            // reset links upon restart
            @Override
            public void onStart() {
                for (Link link : tp.getLinks()){
                    link.setWidth(1);
                }
            }
        });
        deployNodes(tp); // optional
        new JViewer(tp);
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
