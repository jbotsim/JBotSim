package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.gen.dynamic.trace.TraceRecorder;
import io.jbotsim.io.format.xml.XMLTraceBuilder;
import io.jbotsim.ui.JViewer;

public class JBotSimRecorder {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: " + JBotSimRecorder.class.getName() + " input-file");
            System.exit(1);
        }
        String traceFileName = args[0];
        try {
            Topology tp = new Topology();
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
