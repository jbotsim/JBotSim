package examples.fancy.vectorracer;

import io.jbotsim.core.Node;

import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class Drone extends VectorNode {
    Point nextCherry;

    @Override
    public void onStart() {
        super.onStart();
        setSensingRange(20);
        setIcon(Icons.DRONE);
        setIconSize(14);
        onPointReached(getLocation());
    }

    @Override
    public void onPointReached(Point point) {
        if (nextCherry == null)
            if (getTopology().getNodes().size() > 1)
                nextCherry = getTopology().getNodes().get(0).getLocation();
            else
                nextCherry = new Point(500, 400);
        travelTo(nextCherry);
    }

    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Cherry) {
            getTopology().removeNode(node);
            nextCherry = null;
        }
    }
}
