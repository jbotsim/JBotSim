package io.jbotsim.dynamicity.movement.trace;

import io.jbotsim.core.Topology;
import io.jbotsim.format.xml.XMLTraceParser;
import io.jbotsim.ui.JViewer;

public class TracePlayerTest {
    public static void main(String[] args) {
        try {
            Topology topology = new Topology();
            new JViewer(topology);
            TracePlayer tp = new TracePlayer(topology, new XMLTraceParser(topology, true));
            tp.loadAndStart(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
