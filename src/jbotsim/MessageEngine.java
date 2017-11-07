/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim;

import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;

import java.util.ArrayList;

public class MessageEngine implements ClockListener {
    protected Topology topology;
    protected boolean debug = false;

    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public void setSpeed(int speed) {
        topology.removeClockListener(this);
        topology.addClockListener(this, speed);
    }

    public void onClock() {
        clearMailboxes();
        processMessages(collectMessages());
    }

    private void clearMailboxes() {
        for (Node node : topology.getNodes())
            node.getMailbox().clear();
    }

    protected ArrayList<Message> collectMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        for (Node n : topology.getNodes()) {
            for (Message m : n.sendQueue) {
                if (m.destination == null)
                    for (Node ng : m.sender.getOutNeighbors())
                        messages.add(m.withDestination(ng));
                else
                    messages.add(m);
            }
            n.sendQueue.clear();
        }
        return messages;
    }

    protected void processMessages(ArrayList<Message> messages) {
        for (Message m : messages)
            if (m.sender.getOutLinkTo(m.destination) != null)
                deliverMessage(m);
            else if (m.retryMode)
                m.sender.sendQueue.add(m);
    }

    protected void deliverMessage(Message m) {
        m.destination.getMailbox().add(m);
        m.destination.onMessage(m);
        for (MessageListener ml : topology.messageListeners)
            ml.onMessage(m);
        if (debug)
            System.err.println(topology.getTime() + ": " + m);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
