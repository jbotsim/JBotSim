package examples.misc.mobilitymodels;

import jbotsim.Node;
import jbotsim.Point;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

/**
 * Created by acasteig on 14/06/15.
 */
public class WayPointMeetingNode extends Node implements ArrivalListener {

    Point refPoint;
    WayPointMover mover;

    @Override
    public void onStart() {
        refPoint = getLocation();
        mover = new WayPointMover(this);
        mover.addArrivalListener(this);
        onArrival();
    }

    @Override
    public void onArrival() {
        if (getTime() % 1000 < 200) {
            mover.addDestination(300, 50);
        }
        else
            mover.addDestination(refPoint.getX()+(Math.random()*100)-50,
                refPoint.getY()+(Math.random()*100)-50);
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(WayPointMeetingNode.class);
        new JViewer(tp);
    }
}
