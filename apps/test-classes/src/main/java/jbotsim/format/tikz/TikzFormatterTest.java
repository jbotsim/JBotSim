package jbotsim.format.tikz;

import jbotsim.Topology;
import jbotsim.format.common.Format;
import jbotsimx.ui.JViewer;

public class TikzFormatterTest {
    // Test
    public static void main(String[] args) {
        Format.setDefaultFormatter(new TikzFormatter());
        Topology tp = new Topology();
        new JViewer(tp);
    }
}
