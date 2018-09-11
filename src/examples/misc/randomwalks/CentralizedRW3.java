package examples.misc.randomwalks;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsim.event.StartListener;
import jbotsimx.topology.TopologyGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by acasteig on 17/06/15.
 */
public class CentralizedRW3 implements ClockListener, StartListener {
    Random random = new Random();
    Topology tp;
    Node current;

    public CentralizedRW3(Topology tp) {
        this.tp = tp;
        tp.addClockListener(this);
        tp.addStartListener(this);
    }

    @Override
    public void onStart() {
        current = tp.getNodes().get(0);
    }

    @Override
    public void onClock() {
        List<Node> neighbors = current.getNeighbors();
        current = neighbors.get(random.nextInt(neighbors.size()));
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
        new CentralizedRW3(tp);
        tp.setClockSpeed(1);
        tp.start();
    }
}
