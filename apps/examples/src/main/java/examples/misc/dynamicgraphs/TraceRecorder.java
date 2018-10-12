package examples.misc.dynamicgraphs;

import io.jbotsim.Node;
import io.jbotsim.Topology;
import io.jbotsim.event.ClockListener;
import io.jbotsim.ui.JViewer;

import io.jbotsim.Point;
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
        new TraceRecorder(tp, "/home/acasteig/trace").start();
        new JViewer(tp);
    }
}
