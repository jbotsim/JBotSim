package examples.basic.moving;

import jbotsim.Node;

/**
 * Created by acasteig on 2/20/15.
 */
public class MovingNode extends Node{
    @Override
    public void onStart() {
        setDirection(Math.random()*2*Math.PI);
    }

    @Override
    public void onClock() {
        move();
        wrapLocation(); // toroidal space
    }
}
