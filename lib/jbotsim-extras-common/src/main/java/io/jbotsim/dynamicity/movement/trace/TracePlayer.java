/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.dynamicity.movement.trace;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class TracePlayer implements ClockListener {
    private Topology topology;
    private TraceFileReader traceFileReader;
    private LinkedList<TraceEvent> events;
    private PriorityQueue<TraceEvent> story;
    private HashMap<Integer, Node> recordedNodes;
    private ArrayList<ReplayTerminatedListener> listeners;

    public interface ReplayTerminatedListener {
        void onReplayTerminated(TracePlayer tracePlayer);
    }

    public TracePlayer(Topology topology, TraceFileReader traceFileReader) {
        this.topology = topology;
        this.traceFileReader = traceFileReader;
        events = new LinkedList<>();
        story = null;
        recordedNodes = new HashMap<>();
        topology.addClockListener(this);
        listeners = new ArrayList<>();
    }

    public void addListener (ReplayTerminatedListener l) {
        listeners.add(l);
    }

    public void removeListener (ReplayTerminatedListener l) {
        listeners.remove(l);
    }

    public void loadAndStart(String filename) throws Exception {
        traceFileReader.parse(filename, this);
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

        if (story.isEmpty()) {
            for (ReplayTerminatedListener l : listeners) {
                l.onReplayTerminated(this);
            }
            story = null;
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
