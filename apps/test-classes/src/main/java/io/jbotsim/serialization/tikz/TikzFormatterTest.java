package io.jbotsim.serialization.tikz;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class TikzFormatterTest {
    // Test
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setTopologySerializer(new TikzTopologySerializer());
//        new FileTopologySerializer().importFromFile();
        new JViewer(tp);
    }
}
