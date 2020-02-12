/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

package examples.misc.dynamicgraphs;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;

import io.jbotsim.core.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by acasteig on 17/05/15.
 */
public class TraceRecorder implements ClockListener {
    Topology tp;
    HashMap<Node,Point> lastPos = new HashMap<Node, Point>();
    BufferedWriter output;


    public TraceRecorder(Topology tp, String filename) {
        this.tp = tp;
        tp.addClockListener(this);
        try {
            output = new BufferedWriter(new FileWriter(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        tp.resetTime();
    }

    protected void writeLine(String s){
        try {
            output.write(s + "\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClock() {
        List<Node> nodes = tp.getNodes();
        for (Node node : nodes){
            if ( ! lastPos.containsKey(node)){
                lastPos.put(node, node.getLocation());
                writeLine(tp.getTime() + " an " + node + " " + node.getX() + " " + node.getY());
            }
            if ( ! lastPos.get(node).equals(node.getLocation())) {
                lastPos.put(node, node.getLocation());
                writeLine(tp.getTime() + " cn " + node + " " + node.getX() + " " + node.getY());
            }
        }
        for (Node node : new ArrayList<Node>(lastPos.keySet())){
            if ( ! nodes.contains(node)) {
                lastPos.remove(node);
                writeLine(tp.getTime() + " dn " + node);
            }
        }
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        // The trace file created here can be generated with the TracePlayer example
        new TraceRecorder(tp, "/tmp/trace").start();
        new JViewer(tp);
        tp.start();
    }
}
