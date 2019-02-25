package examples.features.replay;

import io.jbotsim.core.Topology;
import io.jbotsim.gen.dynamic.trace.TracePlayer;
import io.jbotsim.gen.dynamic.trace.TraceRecorder;
import io.jbotsim.io.format.xml.XMLParser;
import io.jbotsim.io.format.xml.XMLTopologyParser;
import io.jbotsim.io.format.xml.XMLTraceBuilder;
import io.jbotsim.io.format.xml.XMLTraceParser;
import io.jbotsim.ui.CommandListener;
import io.jbotsim.ui.JViewer;

public class MainReplay implements CommandListener, TracePlayer.ReplayTerminatedListener {
    private static final String START_RECORDER = "Start recorder";
    private static final String STOP_RECORDER = "Stop recorder";
    private static final String REPLAY_TRACE = "Replay trace";

    private static final String TRACE_FILENAME = "trace.xml";

    private Topology topology;
    private JViewer jViewer;
    private TraceRecorder recorder = null;
    private TracePlayer player = null;

    public MainReplay() throws XMLParser.ParserException {
        topology = new Topology();
        XMLTopologyParser parser = new XMLTopologyParser(topology, true);
        parser.parse(getClass().getResourceAsStream("/examples/features/replay/network.xml"));

        jViewer = new JViewer(topology);
        jViewer.getJTopology().addCommandListener(this);
        jViewer.getJTopology().addCommand(START_RECORDER);
        jViewer.getJTopology().addCommand(STOP_RECORDER);
        jViewer.getJTopology().addCommand(REPLAY_TRACE);
    }

    @Override
    public void onCommand(String command) {
        try {
            if (command.equals(START_RECORDER)) {
                XMLTraceBuilder xmlTopologyBuilder = new XMLTraceBuilder(topology);
                recorder = new TraceRecorder(topology, xmlTopologyBuilder);
                recorder.start();
            } else if (command.equals(STOP_RECORDER)) {
                recorder.stopAndWrite(TRACE_FILENAME);
                recorder = null;
            } else if (command.equals(REPLAY_TRACE)) {
                if (recorder != null) {
                    recorder.stopAndWrite(TRACE_FILENAME);
                    recorder = null;
                }
                topology.clear();
                player = new TracePlayer(topology, new XMLTraceParser(topology.getFileManager(), true));
                player.loadAndStart(TRACE_FILENAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReplayTerminated(TracePlayer tracePlayer) {
        player = null;
    }

    public void start() {
        topology.start();
    }

    public static void main(String[] args) {
        try {
            MainReplay main = new MainReplay();
            main.start ();
        } catch (XMLParser.ParserException e) {
            e.printStackTrace();
        }
    }
}
