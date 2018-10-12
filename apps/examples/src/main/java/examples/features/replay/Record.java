package examples.features.replay;

import io.jbotsim.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.format.xml.XMLTopologyParser;
import io.jbotsim.replay.TraceRecorder;

public class Record {
    public static void main(String[] args) {
        try {
            Topology topology = new Topology();
            XMLTopologyParser parser = new XMLTopologyParser(topology);
            parser.parse(Record.class.getResourceAsStream("/examples/features/replay/network.xml"));
            TraceRecorder recorder = new TraceRecorder(topology);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        recorder.stopAndWrite("trace.xml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            recorder.start();

            new JViewer(topology);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
