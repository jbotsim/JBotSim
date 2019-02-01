package examples.funny.wolfsheep;

import io.jbotsim.core.Node;

/**
 * Created by acasteig on 31/08/16.
 */
public class Wolf extends Node {
    private int speed = 2;

    @Override
    public void onStart() {
        setIcon("/io/jbotsim/ui/wolf.png");
        setSize(20);
        setSensingRange(50);
        setDirection(Math.random() * Math.PI * 2);
    }

    @Override
    public void onClock() {
        move(speed);
        wrapLocation();
    }

    @Override
    public void onPostClock() {
        if (Math.random() < 0.005){
            getTopology().removeNode(this);
        }
    }

    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Sheep){
            ((Sheep) node).kill();
        }
    }
}
