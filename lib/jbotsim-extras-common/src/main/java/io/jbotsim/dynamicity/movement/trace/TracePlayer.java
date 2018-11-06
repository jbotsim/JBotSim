package io.jbotsim.dynamicity.movement.trace;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.io.serialization.xml.XMLParser;
import io.jbotsim.io.serialization.xml.XMLTraceParser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by acasteig on 17/05/15.
 */
public class TracePlayer implements ClockListener {
    private Topology topology;
    private LinkedList<TraceEvent> events;
    private PriorityQueue<TraceEvent> story;
    private HashMap<Integer, Node> recordedNodes;

    public TracePlayer(Topology topology) {
        this.topology = topology;
        events = new LinkedList<>();
        story = null;
        recordedNodes = new HashMap<Integer, Node>();
        topology.addClockListener(this);
    }

    public void loadAndStart(String filename) throws XMLParser.ParserException {
        XMLTraceParser parser = new XMLTraceParser(this);
        parser.parse(filename);
        start();
    }

    public void start() {
        topology.resetTime();
        if(! events.isEmpty()) {
            story = new PriorityQueue<>(events.size(), (e1, e2) -> {
                return e1.getTime() - e2.getTime();
            });
            story.addAll(events);
        }
    }

    public Topology getTopology() {
        return topology;
    }

    public void addTraceEvent(TraceEvent e) {
        events.add(e);
    }

    @Override
    public void onClock() {
        if (story == null)
            return;
        while (!story.isEmpty() && story.peek().getTime() <= topology.getTime()) {
            TraceEvent e = story.remove();
            runEvent(e);
        }
    }

    private void runEvent(TraceEvent e) {
        int id = e.getNodeID();
        Node n = recordedNodes.get(id);
        if (n == null) {
            n = topology.findNodeById(id);
        }
        switch (e.getKind()) {
            case START_TOPOLOGY:
                topology.start();
                break;

            case ADD_NODE:
                if (n != null) {
                    System.err.println("node ID already created '" + id + "'");
                    return;
                }
                String className = e.getNodeClass();
                Class<? extends Node> classNode = topology.getNodeModel(className);
                if (classNode == null) {
                    try {
                        classNode = Class.forName(className).asSubclass(Node.class);
                    } catch (ClassNotFoundException ex) {
                        System.err.println(ex.getMessage());
                        classNode = Node.class;
                    }
                }
                try {
                    n = classNode.newInstance();
                    n.setLocation(e.getX(), e.getY());
                    topology.addNode(n);
                    recordedNodes.put(id, n);
                } catch (ReflectiveOperationException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case SELECT_NODE:
                if (n != null) {
                    topology.selectNode(n);
                } else {
                    System.err.println("can not select node '" + id + "'");
                }
                break;
            case MOVE_NODE:
                if (n != null) {
                    n.setLocation(e.getX(), e.getY());
                } else {
                    System.err.println("can not move node '" + id + "'");
                }
                break;
            case DEL_NODE:
                if (n != null) {
                    recordedNodes.remove(id);
                    topology.removeNode(n);
                } else {
                    System.err.println("can not delete node '" + id + "'");
                }
                break;
        }
    }
}
