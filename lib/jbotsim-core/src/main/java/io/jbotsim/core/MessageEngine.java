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
package io.jbotsim.core;

import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.MessageListener;

import java.util.*;

/**
 * <p>The {@link MessageEngine} is responsible for regularly (it is a {@link ClockListener}) transmitting available
 * {@link Message}s from any sender {@link Node} of the {@link Topology} to their destination.</p>
 */
public class MessageEngine implements ClockListener {

    /**
     * The delay value to use for the shortest delivery time possible; value: {@value #DELAY_INSTANT}.
     */
    public static final int DELAY_INSTANT = 1;

    /**
     * The default number of round before message delivery; value: {@value #DELAY_INSTANT}.
     */
    public static final int DEFAULT_DELAY = DELAY_INSTANT;
    private int delay;

    // LinkedHashMap allows insertion order to be kept
    protected Map<Message, Integer> delayedMessages = new LinkedHashMap<>();

    protected Topology topology;
    protected boolean debug = false;

    /**
     * <p>Creates a {@link MessageEngine}.</p>
     * <p>By default, the messages sent during one round are actually during the next.</p>
     *
     * @param topology the {@link Topology} to use.
     */
    public MessageEngine (Topology topology) {
        this(topology, DEFAULT_DELAY);
    }

    /**
     * <p>Creates a {@link MessageEngine}.</p>
     *
     * @param topology the {@link Topology} to use.
     * @param delay the number of round a message should be delayed, as an integer.
     */
    public MessageEngine(Topology topology, int delay) {
        assert(delay >= 0);
        setDelay(delay);
        this.topology = topology;
    }

    /**
     * <p>Sets the {@link Topology} to which the {@link MessageEngine} refers.</p>
     * @param topology a {@link Topology}.
     */
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    /**
     * <p>Sets the number of round a message should be delayed.</p>
     * @param speed the number of round a message should be delayed, as an integer.
     * @deprecated Please use {@link #setDelay(int)} instead.
     */
    @Deprecated
    public void setSpeed(int speed) {
        setDelay(speed);
    }

    /**
     * <p>Sets the number of round a message should be delayed.</p>
     * <p>Any value below {@link #DELAY_INSTANT} (i.e. {@value #DELAY_INSTANT}) will be replaced by
     * {@link #DELAY_INSTANT}.</p>
     *
     * @param delay the number of round a message should be delayed, as an integer.
     */
    public void setDelay(int delay) {
        if(delay < DELAY_INSTANT)
            this.delay = DELAY_INSTANT;
        else
            this.delay = delay;
    }

    /**
     * <p>Gets the number of round a message should be delayed.</p>
     *
     * @return the number of round a message should be delayed, as an integer.
     */
    public int getDelay() {
        return delay;
    }

    @Override
    public void onClock() {
        clearMailboxes();

        bufferizeNewMessages(collectMessages());

        decrementDelays();

        processWaitingMessages();
    }

    protected void clearMailboxes() {
        for (Node node : topology.getNodes())
            node.getMailbox().clear();
    }

    protected void decrementDelays() {
        for (Message m : delayedMessages.keySet())
            delayedMessages.put(m, delayedMessages.get(m)-1);
    }

    protected void bufferizeNewMessages(List<Message> messages) {
        for (Message m : messages)
            delayedMessages.put(m, getDelayForMessage(m));
    }

    protected int getDelayForMessage(Message message) {
        return delay;
    }

    protected List<Message> collectMessages() {
        List<Message> messages = new ArrayList<>();
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

    protected void processWaitingMessages() {
        for (Message m : new ArrayList<>(delayedMessages.keySet()))
            if (!isMessageStillRelevant(m))
                removeWithRetry(m);
            else if (isMessageReadyToGo(m)) {
                sendWithRetry(m);
                delayedMessages.remove(m);
            }
    }

    protected boolean isMessageStillRelevant(Message m) {
        Node sender = m.getSender();
        Node destination = m.getDestination();

        return topology.getLink(sender, destination) != null;
    }

    private void removeWithRetry(Message m) {
        if (m.retryMode)
            requeueMessage(m);

        delayedMessages.remove(m);
    }

    protected boolean requeueMessage(Message m) {
        List<Node> nodes = topology.getNodes();

        if(nodes.contains(m.sender) && nodes.contains(m.destination))
            return m.sender.sendQueue.add(m);
        else
            return false;
    }

    protected boolean isMessageReadyToGo(Message m) {
        return delayedMessages.get(m)<=0;
    }

    protected void processMessages(List<Message> messages) {
        for (Message m : messages)
            sendWithRetry(m);
    }

    protected void sendWithRetry(Message m) {
        if (m.sender.getOutLinkTo(m.destination) != null)
            deliverMessage(m);
        else if (m.retryMode)
            requeueMessage(m);
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

    /**
     * <p>Resets the {@link MessageEngine}.</p>
     * <ul>
     *   <li>Any {@link Message} (ready to be sent, "in the air" or ready to be received) handled by the
     * {@link MessageEngine} is discarded.</li>
     *   <li>Other configurations (delay or debug) remain untouched.</li>
     * </ul>
     */
    public void reset() {
        delayedMessages.clear();
        clearMailboxes();
        clearSendQueues();
    }

    protected void clearSendQueues() {
        for (Node node: topology.getNodes())
            node.sendQueue.clear();
    }

}
