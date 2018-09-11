package examples.misc.mobilitymodels;

import jbotsim.Node;
import jbotsim.event.ClockListener;

import jbotsim.Point;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by acasteig on 14/06/15.
 */
public class WayPointMover implements ClockListener{
    Node node;
    Queue<Point> destinations = new LinkedList<Point>();
    ArrivalListener listener;

    public WayPointMover(Node node) {
        this.node = node;
        node.getTopology().addClockListener(this);
    }

    public void addArrivalListener(ArrivalListener listener){
        this.listener = listener;
    }

    public void addDestination(double x, double y){
        destinations.add(new Point(x, y));
    }

    @Override
    public void onClock() {
        if (destinations.size() > 0) {
            Point dest = destinations.peek();
            if (node.distance(dest) >= 1) {
                node.setDirection(dest);
                node.move();
            }else {
                listener.onArrival();
                destinations.remove();
            }
        }
    }
}
