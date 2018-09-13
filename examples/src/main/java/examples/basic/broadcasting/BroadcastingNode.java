package examples.basic.broadcasting;

import jbotsim.Color;
import jbotsim.Message;
import jbotsim.Node;



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
        if ( ! informed ){
            informed = true;
            setColor(Color.red);
            sendAll(message);
        }
    }
}
