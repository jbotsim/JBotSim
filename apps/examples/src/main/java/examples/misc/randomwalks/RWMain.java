package examples.misc.randomwalks;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;

/**
 * Created by acasteig on 17/06/15.
 */
public class RWMain {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(RWNode1.class);
        new JViewer(tp);
    }
}
