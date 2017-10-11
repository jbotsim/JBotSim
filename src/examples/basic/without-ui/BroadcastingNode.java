package examples.basic.withoutui;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.Color;

/**
 * Created by acasteig on 10/03/15.
 */
public class BroadcastingNode extends Node{

    static int nodeCount;
    static int informedNodeCount;

    boolean informed;

    @Override
    public void onStart() {
        informed = false;
        setColor(null); // optional (for restart only)
    }

    @Override
    public void onSelection() {
        informed = true;
        informedNodeCount++;
        setColor(Color.red);
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message message) {
        if ( ! informed ){
            informedNodeCount++;
            informed = true;
            setColor(Color.red);
            sendAll(message);
            if(nodeCount == informedNodeCount) {
              System.out.println("Finished at time "+getTopology().getTime());
              getTopology().restart();
            }
        }
    }
}
