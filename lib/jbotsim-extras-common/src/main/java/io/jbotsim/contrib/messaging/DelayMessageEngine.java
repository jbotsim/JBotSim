/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.contrib.messaging;

import io.jbotsim.core.Message;
import io.jbotsim.core.MessageEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
