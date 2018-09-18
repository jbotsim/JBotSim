package examples.fancy.canadairs;

import jbotsim.Node;

import java.util.ArrayList;
import java.util.Random;

/* Gardez cette classe telle quelle */
public class Fire extends Node {
    static ArrayList<Fire> allFires = new ArrayList<Fire>();
    Random r=new Random();

	public Fire(){
        disableWireless();
        allFires.add(this);
        setIcon("/examples/fancy/canadairs/fire.png");
        setSize(10);
	}
	public void onClock(){
        if (Math.random() < 0.01)
            propagate();
	}
    public void propagate(){
        if (allFires.size() < 100) {
            double x = getX() + r.nextDouble() * 20 - 10;
            double y = getY() + r.nextDouble() * 20 - 10;
            for (Fire fire : allFires)
                if (fire.distance(x, y) < 10)
                    return;
            getTopology().addNode(x, y, new Fire());
        }
    }
    public void die(){
        allFires.remove(this);
        getTopology().removeNode(this);
    }
}
