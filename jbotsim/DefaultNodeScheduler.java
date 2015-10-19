package jbotsim;

import jbotsim.event.ClockListener;
import jbotsim.ui.painting.NodePainter;

import java.util.HashMap;

/**
 * Created by acasteig on 10/19/15.
 */
public class DefaultNodeScheduler implements NodeScheduler {

    @Override
    public void onClock(Topology tp) {
        for (Node node : tp.getNodes())
            node.onPreClock();
        for (Node node : tp.getNodes())
            node.onClock();
        for (Node node : tp.getNodes())
            node.onPostClock();
    }
}
