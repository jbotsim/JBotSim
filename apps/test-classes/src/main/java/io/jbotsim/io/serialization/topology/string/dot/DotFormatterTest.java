package io.jbotsim.io.serialization.topology.string.dot;

import io.jbotsim.core.Topology;
import io.jbotsim.io.serialization.topology.FileTopologySerializer;
import io.jbotsim.io.serialization.topology.string.dot.DotTopologySerializer;
import io.jbotsim.ui.JViewer;

public class DotFormatterTest {
    // Test
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.disableWireless();
        String filename = "/home/acasteig/test.dot"; // to be updated
        new FileTopologySerializer().importFromFile(tp, filename, new DotTopologySerializer());
        new JViewer(tp);
    }
}
