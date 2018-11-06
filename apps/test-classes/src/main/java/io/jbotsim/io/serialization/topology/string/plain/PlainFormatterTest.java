package io.jbotsim.io.serialization.topology.string.plain;

import io.jbotsim.core.Topology;
import io.jbotsim.io.serialization.topology.FileTopologySerializer;
import io.jbotsim.io.serialization.topology.string.plain.PlainTopologySerializer;
import io.jbotsim.ui.JViewer;

public class PlainFormatterTest {
    // Test
    public static void main(String[] args) {
        String filename = "/home/acasteig/test.plain"; // to be updated
        Topology tp = new Topology();
        tp = new FileTopologySerializer().importFromFile(tp, filename, new PlainTopologySerializer());
        new JViewer(tp);
    }
}
