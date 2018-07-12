package examples.replay;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

/**
 * Created by acasteig on 10/03/15.
 */
public class BroadcastingNode extends Node{
    boolean informed;

    @Override
    public void onStart() {
        informed = false;
        setColor(null); // optional (for restart only)
    }

    @Override
    public void onSelection() {
        informed = true;
        setColor(Color.red);
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message message) {
        if (! (message.getSender() instanceof BroadcastingNode) )
            return;

        if ( ! informed ){
            informed = true;
            setColor(Color.red);
            sendAll(message);
        }
    }
}
