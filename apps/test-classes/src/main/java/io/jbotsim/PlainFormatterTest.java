package io.jbotsim;

import io.jbotsim.Topology;
import io.jbotsim.format.common.Format;
import io.jbotsim.format.plain.PlainFormatter;
import io.jbotsim.ui.JViewer;

public class PlainFormatterTest {
    // Test
    public static void main(String[] args) {
        String filename = "/home/acasteig/test.plain"; // to be updated
        Topology tp = Format.importFromFile(filename, new PlainFormatter());
        new JViewer(tp);
    }
}
