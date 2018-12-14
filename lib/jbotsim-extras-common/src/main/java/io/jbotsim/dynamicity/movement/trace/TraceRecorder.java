package io.jbotsim.dynamicity.movement.trace;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.MovementListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.core.event.TopologyListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by acasteig on 17/05/15.
 */
public class TraceRecorder implements MovementListener, SelectionListener, TopologyListener, StartListener {
    private TraceFileWriter traceFileWriter;
    private Topology tp;
    private List<TraceEvent> story;

    public TraceRecorder(Topology tp, TraceFileWriter traceFileWriter) {
        this.traceFileWriter = traceFileWriter;
        this.tp = tp;
        story = new LinkedList<>();
        tp.addMovementListener(this);
        tp.addSelectionListener(this);
        tp.addTopologyListener(this);
        tp.addStartListener(this);
    }

    @Override
    public void onStart() {
        story.add(TraceEvent.newStartTopology(tp.getTime()));
    }

    @Override
    public void onMove(Node node) {
        story.add(TraceEvent.newMoveNode(tp.getTime(), node.getID(), node.getX(), node.getY()));
    }

    @Override
    public void onSelection(Node node) {
        story.add(TraceEvent.newSelectNode(tp.getTime(), node.getID()));
    }

    @Override
    public void onNodeAdded(Node node) {
        story.add(TraceEvent.newAddNode(tp.getTime(), node.getID(), node.getX(), node.getY(),
                node.getClass().getName()));
    }

    @Override
    public void onNodeRemoved(Node node) {
        story.add(TraceEvent.newDeleteNode(tp.getTime(), node.getID()));
    }

    public void start(){
        tp.resetTime();
        story.clear();
    }

    public void stopAndWrite(String filename) throws Exception {
        for(TraceEvent e : story) {
            traceFileWriter.addTraceEvent(e);
        }
        traceFileWriter.write(filename);
    }

}
