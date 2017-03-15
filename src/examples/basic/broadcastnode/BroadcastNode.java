package examples.basic.broadcastnode;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.Color;

/**
 * Created by acasteig on 10/03/15.
 */
public class BroadcastNode extends Node{
    boolean informed;

    @Override
    public void onStart() {
        informed = false;
        setColor(null);
    }

    @Override
    public void onSelection() {
        informed = true;
        setColor(Color.red);
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message message) {
        if ( ! informed ){
            informed = true;
            setColor(Color.red);
            sendAll(message);
        }
    }
}
