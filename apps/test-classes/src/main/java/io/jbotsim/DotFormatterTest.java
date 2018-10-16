package io.jbotsim;

import io.jbotsim.format.dot.DotFormatter;
import io.jbotsim.core.Topology;
import io.jbotsim.format.common.Format;
import io.jbotsim.ui.JViewer;

public class DotFormatterTest {
    // Test
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.disableWireless();
        String filename = "/home/acasteig/test.dot"; // to be updated
        Format.importFromFile(tp, filename, new DotFormatter());
        new JViewer(tp);
    }
}
