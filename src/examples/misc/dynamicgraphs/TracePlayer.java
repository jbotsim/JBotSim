package examples.misc.dynamicgraphs;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsimx.ui.JViewer;

import jbotsim.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by acasteig on 17/05/15.
 */
public class TracePlayer implements ClockListener {
    Topology tp;
    HashMap<Node,Point> lastPos = new HashMap<Node, Point>();
    BufferedReader input;
    Line line;


    protected class Line{
        int round;
        String operation;
        int id;
        Point coords;

        public Line(String line) {
            StringTokenizer st = new StringTokenizer(line, " ");
            round = Integer.parseInt(st.nextToken());
            operation = st.nextToken();
            id = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                double x = Double.parseDouble(st.nextToken());
                double y = Double.parseDouble(st.nextToken());
                coords = new Point(x, y);
            }
        }
    }
    public TracePlayer(Topology tp, String filename) {
        this.tp = tp;
        tp.addClockListener(this);
        try {
            input = new BufferedReader(new FileReader(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        tp.resetTime();
        readLine();
    }

    protected void readLine(){
        try {
            String s = input.readLine();
            line = (s == null) ? null : new Line(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClock() {
        while (line != null && tp.getTime() == line.round){
            if (line.operation.equals("an")){
                Node node = new Node(); // fixme
                node.setLocation(line.coords);
                node.setID(line.id);
                tp.addNode(node);
            }else if (line.operation.equals("dn")){
                tp.removeNode(tp.findNodeById(line.id));
            }else if (line.operation.equals("cn")){
                Node node = tp.findNodeById(line.id);
                node.setLocation(line.coords);
            }
            readLine();
        }
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        new TracePlayer(tp, "/home/acasteig/trace").start();
        new JViewer(tp);
    }
}
