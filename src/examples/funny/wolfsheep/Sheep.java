package examples.funny.wolfsheep;

import jbotsim.Node;
import jbotsim.Topology;

/**
 * Created by acasteig on 31/08/16.
 */
public class Sheep extends Node {
    private int speed = 1;
    private boolean isAlive = true;

    @Override
    public void onStart() {
        setIcon("/examples/wolfsheep/sheep.png");
        setSize(12);
        setDirection(Math.random() * Math.PI * 2);
    }

    @Override
    public void onClock() {
        move(speed);
        wrapLocation();
    }

    @Override
    public void onPostClock() {
        Topology tp = getTopology();
        if ( ! isAlive ) {
            tp.removeNode(this);
            if (Math.random() < 0.5){
                tp.addNode(-1, -1, new Wolf());
            }
        }else{
            if (Math.random() < 0.01){
                tp.addNode(-1, -1, new Sheep());
            }
        }
    }

    public void kill(){
        isAlive = false;
    }
}
