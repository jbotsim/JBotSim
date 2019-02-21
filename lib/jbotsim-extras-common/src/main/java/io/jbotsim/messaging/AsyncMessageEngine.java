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
package io.jbotsim.messaging;

import io.jbotsim.core.Message;
import io.jbotsim.core.MessageEngine;
import io.jbotsim.core.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class AsyncMessageEngine extends MessageEngine {
    public static enum Type{FIFO, NONFIFO};

    protected HashMap<Message, Integer> delays = new LinkedHashMap<Message, Integer>();
    protected double average;
    protected Random r = new Random();
    protected Type type = Type.FIFO;

    public AsyncMessageEngine(double averageDuration, Type type){
        assert(average > 0);
        this.average = averageDuration;
        this.type = type;
    }

    protected int drawDelay(Message m){
        if (type == Type.FIFO){
            int max = 0;
            Node sender = m.getSender();
            Node destination = m.getDestination();
            for (Message m2 : delays.keySet())
                if (m2.getSender() == sender && m2.getDestination() == destination)
                    max = Math.max(max, delays.get(m2));

            return (int) (max + Math.round(Math.log(1 - Math.random()) / (-1.0/average)));
        }else{
            return r.nextInt((int)Math.round(average));
        }
    }

    public void onClock(){
        for (Message m : delays.keySet())
            delays.put(m, delays.get(m)-1);
        for (Message m : super.collectMessages())
            delays.put(m, drawDelay(m));
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
