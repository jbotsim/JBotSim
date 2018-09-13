package examples.misc.randomwalks;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsim.event.StartListener;
import jbotsimx.topology.TopologyGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by acasteig on 17/06/15.
 */
public class CentralizedRW4 implements ClockListener, StartListener {
    Random random = new Random();
    Topology tp;
    Node current;

    public CentralizedRW4(Topology tp) {
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
        List<Integer> stats = (List<Integer>) tp.getProperty("stats");
        if (stats == null)
            tp.setProperty("stats", stats=new ArrayList<Integer>());
        stats.add(tp.getTime());
        if (stats.size()==100) {
            double total = 0.0;
            for (Integer I : stats)
                total += I;
            System.out.println(tp.getNodes().size() + "\t" + total/100);
            tp.pause();
        }else {
            tp.restart();
        }
    }

    public static void main(String[] args) {
        for (int i=3; i<=10; i++) {
            Topology tp = new Topology();
            TopologyGenerator.generateLine(tp, i);
            new CentralizedRW4(tp);
            tp.setClockSpeed(1);
            tp.start();
        }
    }
}
