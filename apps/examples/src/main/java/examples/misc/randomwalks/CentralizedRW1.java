package examples.misc.randomwalks;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.topology.TopologyGenerator;

import io.jbotsim.core.Color;
import java.util.List;
import java.util.Random;

/**
 * Created by acasteig on 17/06/15.
 */
public class CentralizedRW1 implements ClockListener {
    Random random = new Random();
    Node current;

    public CentralizedRW1(Topology tp) {
        tp.addClockListener(this);
        current = tp.getNodes().get(0);
        current.setColor(Color.black);
    }

    @Override
    public void onClock() {
        List<Node> neighbors = current.getNeighbors();
        current.setColor(null);
        current = neighbors.get(random.nextInt(neighbors.size()));
        current.setColor(Color.black);
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        TopologyGenerator.generateLine(tp, 10);
        new CentralizedRW1(tp);
        new JViewer(tp);
    }
}
