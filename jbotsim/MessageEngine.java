package jbotsim;

import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by acasteig on 2/20/15.
 */
public class MessageEngine implements ClockListener {
    protected HashMap<Message, Integer> currentMessages=new HashMap<Message,Integer>();
    protected Topology topology;
    protected boolean debug=false;

    public void setTopology(Topology topology){
        this.topology = topology;
    }
    public void onClock(){
        for (Node n : topology.nodes){
            ArrayList<Message> toRemove=new ArrayList<Message>();
            for (Message m : n.sendQueue){
                if (m.destination!=null && m.source.getOutLinkTo(m.destination)==null){
                    if (!m.retryMode)
                        toRemove.add(m);
                }else{
                    currentMessages.put(m, m.messageDelay);
                    toRemove.add(m);
                }
            }
            n.sendQueue.removeAll(toRemove);
        }
        for (Message m : new HashMap<Message,Integer>(currentMessages).keySet()){
            int remainingDelay=currentMessages.get(m);
            if (remainingDelay==1){
                currentMessages.remove(m);
                if (m.destination!=null){
                    if (m.source.getOutLinkTo(m.destination)!=null)
                        deliverMessageTo(m, m.destination);
                    else{
                        if (m.retryMode)
                            m.source.sendQueue.add(m);
                    }
                }else
                    for(Link l : m.source.getOutLinks())
                        deliverMessageTo(m, l.destination);
            }else{
                if (m.source.getOutLinkTo(m.destination)!=null){
                    currentMessages.put(m, remainingDelay-1);
                }else{
                    currentMessages.remove(m);
                    if (m.retryMode)
                        m.source.sendQueue.add(m);
                }
            }
        }
    }
    protected void deliverMessageTo(Message m, Node dest){
        dest.mailBox.add(m);
        if (debug)
            System.err.println((Clock.currentTime()-m.messageDelay)+"->"+Clock.currentTime()+": "+
                    m.source+ "->" + dest+": "+m.content+" ("+
                    m.content.getClass().getSimpleName()+")");
        for (MessageListener ml : dest.messageListeners)
            ml.onMessage(m);
        for (MessageListener ml : dest.topo.messageListeners)
            ml.onMessage(m);
    }
    public void setDebug(boolean debug){
        this.debug=debug;
    }
}
