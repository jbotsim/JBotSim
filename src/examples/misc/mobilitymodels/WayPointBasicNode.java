package examples.misc.mobilitymodels;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

import jbotsim.Point;

/**
 * Created by acasteig on 14/06/15.
 */
public class WayPointBasicNode extends Node implements ArrivalListener{

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
        mover.addDestination(refPoint.getX()+(Math.random()*100)-50,
                refPoint.getY()+(Math.random()*100)-50);
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(WayPointBasicNode.class);
        new JViewer(tp);
    }
}
