package jbotsim;

import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;

import java.util.ArrayList;

/**
 * Created by acasteig on 2/20/15.
 */
public class MessageEngine implements ClockListener {
    protected Topology topology;
    protected boolean debug=false;

    public void setTopology(Topology topology){
        this.topology = topology;
    }
    public void setSpeed(int speed){
        topology.removeClockListener(this);
        topology.addClockListener(this, speed);
    }
    public void onClock(){
        processMessages(collectMessages());
    }
    protected ArrayList<Message> collectMessages(){
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Node n : topology.getNodes()) {
            for (Message m : n.sendQueue) {
                if (m.destination == null)
                    for (Node ng : m.sender.getOutNeighbors())
                        messages.add(new Message(n, ng, m.content));
                else
                    messages.add(m);
            }
            n.sendQueue.clear();
        }
        return messages;
    }
    protected void processMessages(ArrayList<Message> messages){
        for (Message m : messages)
            if (m.sender.getOutLinkTo(m.destination) != null)
                deliverMessage(m);
            else if (m.retryMode)
                m.sender.sendQueue.add(m);
    }
    protected void deliverMessage(Message m){
        m.destination.onMessage(m);
        for (MessageListener ml : topology.messageListeners)
            ml.onMessage(m);
        if (debug)
            System.err.println(topology.getTime()+": " + m);
    }
    public void setDebug(boolean debug){
        this.debug=debug;
    }
}
