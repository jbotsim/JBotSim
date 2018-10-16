package io.jbotsim.format.tikz;

import io.jbotsim.core.Topology;
import io.jbotsim.format.common.Format;
import io.jbotsim.ui.JViewer;

public class TikzFormatterTest {
    // Test
    public static void main(String[] args) {
        Format.setDefaultFormatter(new TikzFormatter());
        Topology tp = new Topology();
        new JViewer(tp);
    }
}
