package examples.basic.mobilebroadcast;

import io.jbotsim.Topology;
import io.jbotsim.ui.JViewer;

/**
 * Created by acasteig on 25/08/15.
 */
public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MobileBroadcastNode.class);
        new JViewer(tp);
    }
}
