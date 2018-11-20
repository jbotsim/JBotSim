package io.jbotsim.serialization.dot;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class DotFormatterTest {
    // Test
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.disableWireless();
        String filename = "/home/acasteig/test.dot"; // to be updated
        tp.setTopologySerializer(new DotTopologySerializer()); // is actually the default
        tp.getTopologySerializer().importTopology(tp, tp.getFileManager().read(filename));
        new JViewer(tp);
    }
}
