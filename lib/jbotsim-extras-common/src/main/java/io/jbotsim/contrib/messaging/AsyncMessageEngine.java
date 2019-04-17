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
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

/**
 * <p>The {@link AsyncMessageEngine} is an asynchronous alternative to JBotSim's default {@link MessageEngine}.</p>
 * <p>It is made to work either as {@link Type#FIFO} or {@link Type#NONFIFO}. Currently, for each new message:</p>
 * <ul>
 *     <li>the {@link Type#NONFIFO} mode uses the provided average duration to compute the delay (<code>f</code>
 *     function discussed thereafter);</li>
 *     <li>the {@link Type#FIFO} mode also uses the <code>f</code> function to compute a theoretical delay, but also
 *     takes the current maximum delay time between the two nodes in order to guaranty a proper FIFO behavior.</li>
 * </ul>
 * <p>In both cases, the <code>f</code> function used to compute the random delay is defined as follows:<br>
 *
 * <code>f(r) = -log(1-r) * {@link AsyncMessageEngine#getAverageDuration()}</code>, where <code>r</code> is a value
 * returned by {@link Math#random()}.</p>
 */
public class AsyncMessageEngine extends MessageEngine {

    /**
     * The default average duration; value: {@value #DEFAULT_AVERAGE_DURATION}.
     */
    public static final int DEFAULT_AVERAGE_DURATION = 10;

    /**
     * The default delivery queue type; value: {@link Type#FIFO}.
     */
    public static final Type DEFAULT_TYPE = Type.FIFO;

    /**
     * Delivery queue type.
     */
    public enum Type{FIFO, NONFIFO}

    protected int averageDuration;
    protected Type type;

    /**
     * <p>Creates a {@link AsyncMessageEngine} object.</p>
     * <p>The default used are:</p>
     * <ul>
     *     <li>{@link #DEFAULT_AVERAGE_DURATION} as default average duration;</li>
     *     <li>{@link #DEFAULT_TYPE} as default type.</li>
     *</ul>
     *
     * @param topology the {@link Topology} to use.
     */
    public AsyncMessageEngine(Topology topology){
        this(topology, DEFAULT_AVERAGE_DURATION, DEFAULT_TYPE);
    }

    /**
     * <p>Creates a {@link AsyncMessageEngine} object.</p>
     *
     * @param topology the {@link Topology} to use.
     * @param averageDuration the desired average number of rounds needed for a message to be delivered to its
     *                        destination, as an integer.
     * @param type the {@link Type} of the delivery queue.
     */
    public AsyncMessageEngine(Topology topology, int averageDuration, Type type){
        super(topology);
        assert(this.averageDuration > 0);
        this.averageDuration = averageDuration;
        this.type = type;
    }

    @Override
    protected int getDelayForMessage(Message message) {
        return drawDelay(message);
    }

    protected int drawDelay(Message m){
        if (type == Type.FIFO){
            Node sender = m.getSender();
            Node destination = m.getDestination();
            int currentMax = computeCurrentMaximumDelay(sender, destination);
            int delayFunction = computeDelayFunction(getAverageDuration());

            int delay = Math.max(currentMax, delayFunction);
            if(debug)
                System.err.println("FIFO delay " + delay + " (currentMax:"+currentMax+", random:"+ delayFunction+")");
            return delay;
        }else{
            int delay = computeDelayFunction(getAverageDuration());
            if(debug)
                System.err.println("NON-FIFO delay " + delay);
            return delay;
        }
    }

    protected int computeCurrentMaximumDelay(Node sender, Node destination) {
        int max = 0;
        for (Message m2 : delayedMessages.keySet())
            if (m2.getSender() == sender && m2.getDestination() == destination)
                max = Math.max(max, delayedMessages.get(m2));
        return max;
    }

    protected static int computeDelayFunction(int lambda) {
        return (int) Math.round(Math.log(1 - Math.random()) / (-1.0 / lambda));
    }

    /**
     * Gets the desired average number of rounds needed for a message to be delivered to its destination.
     * @return the desired average number of rounds needed for a message to be delivered to its destination, as
     * an Integer.
     */
    public int getAverageDuration() {
        return averageDuration;
    }

    /**
     * Sets the desired average number of rounds needed for a message to be delivered to its destination.
     * @param averageDuration the desired average number of rounds needed for a message to be delivered to its
     *                        destination, as an Integer.
     */
    public void setAverageDuration(int averageDuration) {
        this.averageDuration = averageDuration;
    }

    /**
     * Please prefer using {@link #setAverageDuration(int)}.
     * @deprecated
     * @see #setAverageDuration(int)
     */
    @Override
    @Deprecated
    public void setDelay(int delay) {
        setAverageDuration(delay);
    }

    /**
     * Please prefer using {@link #getAverageDuration()}.
     * @deprecated
     * @see #getAverageDuration()
     */
    @Override
    @Deprecated
    public int getDelay() {
        return getAverageDuration();
    }
}
