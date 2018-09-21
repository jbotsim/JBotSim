package examples.misc.randomwalks;

import jbotsim.Node;

import jbotsim.Color;
import java.util.Random;

/**
 * Created by acasteig on 17/06/15.
 */
public class RWNode2 extends Node {
    Random random = new Random();
    boolean willMove = false;

    @Override
    public void onSelection() {
        setColor(Color.black);
    }

    @Override
    public void onPreClock() {
        if (getColor() == Color.black)
            willMove = true;
    }

    @Override
    public void onClock() {
        if (willMove) {
            Node next = getNeighbors().get(random.nextInt(getNeighbors().size()));
            next.setColor(Color.black);
            setColor(null);
            willMove = false;
        }
    }
}
