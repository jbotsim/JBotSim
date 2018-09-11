package examples.misc.randomwalks;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsim.event.StartListener;
import jbotsimx.ui.JViewer;
import jbotsimx.topology.TopologyGenerator;

import jbotsim.Color;
import java.util.List;
import java.util.Random;

/**
 * Created by acasteig on 17/06/15.
 */
public class CentralizedRW2 implements ClockListener, StartListener {
    Random random = new Random();
    Topology tp;
    Node current;

    public CentralizedRW2(Topology tp) {
        this.tp = tp;
        tp.addClockListener(this);
        tp.addStartListener(this);
    }

    @Override
    public void onStart() {
        for (Node node : tp.getNodes())
            node.setColor(null);
        current = tp.getNodes().get(0);
        current.setColor(Color.black);
    }

    @Override
    public void onClock() {
        List<Node> neighbors = current.getNeighbors();
        current.setColor(null);
        current = neighbors.get(random.nextInt(neighbors.size()));
        current.setColor(Color.black);
        if (current == tp.getNodes().get(tp.getNodes().size()-1))
            finish();
    }

    public void finish(){
        System.out.println(tp.getTime());
        tp.restart();
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        TopologyGenerator.generateLine(tp, 10);
        new CentralizedRW2(tp);
        new JViewer(tp);
        tp.start();
    }
}
