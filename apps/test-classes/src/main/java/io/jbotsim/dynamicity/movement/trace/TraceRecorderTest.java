package io.jbotsim.dynamicity.movement.trace;

import io.jbotsim.core.Topology;
import io.jbotsim.io.serialization.topology.string.xml.XMLTopologyBuilder;
import io.jbotsim.io.serialization.trace.xml.XMLTraceBuilder;
import io.jbotsim.ui.JViewer;

public class TraceRecorderTest {
    public static void main(String[] args) {
        try {
            Topology tp = new Topology();
            String traceFileName = args[0];
            TraceRecorder tr = new TraceRecorder(tp, new XMLTraceBuilder(tp));

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        tr.stopAndWrite(traceFileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            tr.start();
            new JViewer(tp);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
