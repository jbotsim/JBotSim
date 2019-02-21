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
import io.jbotsim.core.event.MovementListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.core.event.TopologyListener;

import java.util.LinkedList;
import java.util.List;

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
