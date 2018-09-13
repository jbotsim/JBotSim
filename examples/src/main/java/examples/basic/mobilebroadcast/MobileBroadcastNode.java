package examples.basic.mobilebroadcast;

import jbotsim.Color;
import jbotsim.Message;
import jbotsim.Node;



/**
 * Created by acasteig on 2/20/15.
 */
public class MobileBroadcastNode extends Node{

    boolean informed;

    @Override
    public void onStart() {
        setDirection(Math.PI * 2 * Math.random());
        informed = false;
        setColor(null); // optional (for restart only)
    }

    @Override
    public void onSelection() {
        informed = true;
        setColor(Color.red);
    }

    @Override
    public void onClock() {
        if (informed)
            sendAll(new Message());
        move();
        wrapLocation();
    }

    @Override
    public void onMessage(Message message) {
        informed = true;
        setColor(Color.red);
    }
}
