package jbotsimx.replay;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.MovementListener;
import jbotsim.event.SelectionListener;
import jbotsim.event.StartListener;
import jbotsim.event.TopologyListener;
import jbotsimx.format.xml.XMLBuilder;
import jbotsimx.format.xml.XMLTraceBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by acasteig on 17/05/15.
 */
public class TraceRecorder implements MovementListener, SelectionListener, TopologyListener, StartListener {
    private XMLTraceBuilder builder;
    private Topology tp;
    private List<TraceEvent> story;

    public TraceRecorder(Topology tp) throws Exception {
        builder = new XMLTraceBuilder(tp);
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

    public void stopAndWrite(String filename) throws XMLBuilder.BuilderException {
        for(TraceEvent e : story) {
            builder.addTraceEvent(e);
        }
        builder.write(filename);
    }

}
