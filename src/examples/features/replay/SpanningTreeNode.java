package examples.features.replay;

import jbotsim.Message;
import jbotsim.Node;

/**
 * Created by acasteig on 10/03/15.
 */
public class SpanningTreeNode extends Node{
    Node parent;

    @Override
    public void onStart() {
        parent = null;
    }

    @Override
    public void onSelection() {
        parent = this;
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message message) {
        if (! (message.getSender() instanceof SpanningTreeNode))
            return;
        if ( parent == null ){
            parent = message.getSender();
            getCommonLinkWith(parent).setWidth(4);
            sendAll(new Message());
        }
    }
}
