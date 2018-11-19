package io.jbotsim.io.serialization.plain;

import io.jbotsim.core.Topology;
import io.jbotsim.serialization.plain.PlainTopologySerializer;
import io.jbotsim.ui.JViewer;

public class PlainFormatterTest {
    // Test
    public static void main(String[] args) {
        String filename = "/home/acasteig/test.plain"; // to be updated
        Topology tp = new Topology();
        tp.setTopologySerializer(new PlainTopologySerializer()); // is actually the default
        tp.getTopologySerializer().importTopology(tp, tp.getFileManager().read(filename));
        new JViewer(tp);
    }
}
