package jbotsimx.messaging;

import jbotsim.Message;
import jbotsim.MessageEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by acasteig on 2/20/15.
 */
public class DelayMessageEngine extends MessageEngine {
    protected HashMap<Message, Integer> delays = new HashMap<Message,Integer>();
    protected int delay = 1; // messages sent in round i are delivered in round i+delay
    protected Random r = new Random();
    protected boolean isRandom = false;

    public DelayMessageEngine(int delay){
        this(false, delay);
    }
    public DelayMessageEngine(boolean randomDelay, int maxDelay){
        assert(maxDelay > 0);
        this.delay = maxDelay;
        this.isRandom = randomDelay;
    }

    public void onClock(){
        for (Message m : delays.keySet())
            delays.put(m, delays.get(m)-1);
        for (Message m : super.collectMessages())
            if (isRandom)
                delays.put(m, r.nextInt(delay));
            else
                delays.put(m,delay);
        for (Message m : new ArrayList<Message>(delays.keySet()))
            if (delays.get(m)==0)
                deliverMessage(m);
    }

    @Override
    protected void deliverMessage(Message m) {
        if (m.getSender().getOutLinkTo(m.getDestination()) != null)
            super.deliverMessage(m);
        delays.remove(m);
    }
}
