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

import io.jbotsim.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>The {@link AsyncMessageEngine} is an asynchronous alternative to JBotSim's default {@link MessageEngine}.</p>
 * <p>It is made to work either as {@link Type#FIFO} or {@link Type#NONFIFO}. Currently, for each new message:</p>
 * <ul>
 *     <li>the {@link Type#NONFIFO} mode uses the provided average duration to compute the delay (<code>f</code>
 *     function discussed thereafter);</li>
 *     <li>the {@link Type#FIFO} mode also uses the <code>f</code> function to compute a theoretical delay, but also
 *     takes the current maximum delay time between the two nodes in order to guaranty a proper FIFO behavior.</li>
 * </ul>
 * <p>In both cases, the <code>f</code> function used to draw the random delay, which follows an exponential
 * distribution law of rate <code>1./{@link #getAverageDuration()}</code>, is computed as such:<br>
 * <code>f(r) = -log(1-r) * {@link AsyncMessageEngine#getAverageDuration()}</code>, where <code>r</code> is a value
 * returned by {@link Math#random()}.</p>
 */
public class AsyncMessageEngine extends DelayMessageEngine {

    /**
     * The default average duration; value: {@value #DEFAULT_AVERAGE_DURATION}.
     */
    public static final int DEFAULT_AVERAGE_DURATION = 10;

    /**
     * The default delivery queue type; value: {@link Type#FIFO}.
     */
    public static final Type DEFAULT_TYPE = Type.FIFO;

    private Random random = new Random();

    private MaximumDeliveryDatesTracker maximumDeliveryDates = new MaximumDeliveryDatesTracker();

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
        super(topology, averageDuration);
        assert(this.averageDuration > 0);
        this.averageDuration = averageDuration;
        this.type = type;
    }

    @Override
    public void onClock() {
        super.onClock();

        if(shouldCleanDeliveryDates())
            maximumDeliveryDates.clearUpTo(currentTime);

    }

    protected boolean shouldCleanDeliveryDates() {
        return true;
    }

    @Override
    protected boolean noCachingNeeded(List<Message> newMessages) {
        return false;
    }

    @Override
    protected Map<Integer, List<Message>> prepareNewMessagesForCaching(List<Message> newMessages) {

        Map<Integer,List<Message>> messagesMap = new HashMap<>();

        for (Message message : newMessages)
            cacheMessageAtTime(messagesMap, message, getDeliveryDateForMessage(message));

        return messagesMap;
    }

    /**
     * <p>Computes the delivery date (round number) for the provided {@link Message}.</p>
     * @param message the {@link Message} needing a delivery date.
     * @return the delivery date (round number) for the message.
     */
    protected int getDeliveryDateForMessage(Message message) {

        int delay = getDelayForMessage(message);
        int deliveryDate = currentTime - 1 + delay;

        updateMaxDeliveryDate(message, deliveryDate);
        return deliveryDate;
    }

    /**
     * <p>Keeps track the maximum delivery date of messages from the sender to the destination of the provided
     * message.</p>
     * @param message the {@link Message}.
     * @param deliveryDate the new maximum delivery date.
     */
    private void updateMaxDeliveryDate(Message message, int deliveryDate) {

        if (type != Type.FIFO)
            return;

        maximumDeliveryDates.put(message.getSender(), message.getDestination(), deliveryDate);

    }

    /**
     * <p>Retrieves the current maximum delivery date for messages between the provided sender and destination.</p>
     * @param sender the sender {@link Node}.
     * @param destination the destination {@link Node}.
     * @return the current maximum delivery date (round number), as an integer.
     */
    protected int getCurrentMaximumDeliveryDate(Node sender, Node destination) {
        return maximumDeliveryDates.get(sender, destination);
    }

    @Override
    protected int getDelayForMessage(Message message) {
        if (type == Type.FIFO)
            return drawDelayFIFO(message);
        else
            return drawDelayNONFIFO(message);
    }

    /**
     * <p>Draws a delay for the provided {@link Message}, in the non-FIFO case.</p>
     * @param message the {@link Message} needing a delay.
     * @return the delay, as an integer.
     */
    protected int drawDelayNONFIFO(Message message) {
        int delay = computeDelayFunction(getAverageDuration());

        if(debug)
            System.err.println("NON-FIFO delay " + delay);

        return delay;
    }

    /**
     * <p>Draws a delay for the provided {@link Message}, in the FIFO case.</p>
     * @param message the {@link Message} needing a delay.
     * @return the delay, as an integer.
     */
    protected int drawDelayFIFO(Message message){
        Node sender = message.getSender();
        Node destination = message.getDestination();
        int currentDelayMax = getCurrentMaximumDeliveryDate(sender, destination) - currentTime + 1;
        int delayFunction = computeDelayFunction(getAverageDuration());

        int delay = Math.max(currentDelayMax, delayFunction);
        if(debug)
            System.err.println("FIFO delay " + delay + " (currentMax:"+currentDelayMax+", random:"+ delayFunction+")");
        return delay;

    }

    /**
     * <p>Computes the next value for the delay function.</p>
     * @param lambda the lambda parameter, as an integer.
     * @return the next value for the delay function, as an integer.
     */
    protected int computeDelayFunction(int lambda) {
        return (int) Math.round(Math.log(1 - random.nextDouble()) / (-1.0 / lambda));
    }

    /**
     * Gets the desired average number of rounds needed for a message to be delivered to its destination.
     * @return the desired average number of rounds needed for a message to be delivered to its destination, as an
     * integer.
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

    private class MaximumDeliveryDatesTracker {
        public static final int DEFAULT_VALUE = -1;

        // senderNode -> (destinationNode -> maximumDeliveryDate)
        private Map<Node, Map<Node, Integer>> maximumDeliveryDates = new HashMap<>();

        /**
         * <p>Fetches the current maximum delivery date of a message from sender to destination.</p>
         *
         * @param sender the sender {@link Node}
         * @param destination the destination {@link Node}
         * @return If relevant, the maximum delivery date of a message going from sender to destination, as an integer.
         *  If not present: {@link #DEFAULT_VALUE}.
         */
        public int get(Node sender, Node destination) {

            Map<Node, Integer> destinationsMap = getDestinationsFor(sender, maximumDeliveryDates);
            if (destinationsMap.isEmpty())
                return DEFAULT_VALUE;

            Integer value = getValueForDestination(destination, destinationsMap);
            if (value == null)
                return DEFAULT_VALUE;

            return value;
        }

        private Integer getValueForDestination(Node destination, Map<Node, Integer> destinationsMap) {
            return destinationsMap.get(destination);
        }

        private Map<Node, Integer> getDestinationsFor(Node sender, Map<Node, Map<Node, Integer>> maximumDeliveryDates) {
            Map<Node, Integer> destinationsMap = maximumDeliveryDates.get(sender);
            if(destinationsMap == null) {
                destinationsMap = new HashMap<>();
                maximumDeliveryDates.put(sender, destinationsMap);
            }
            return destinationsMap;
        }

        /**
         * <p>Puts the new maximum delivery date of a message from sender to destination, without checking consistency
         * with the previous value.</p
         * @param sender the sender {@link Node}
         * @param destination the destination {@link Node}
         * @param deliveryDate the new maximum delivery date, as an integer.
         */
        public void put(Node sender, Node destination, int deliveryDate) {
            put(sender, destination, deliveryDate, maximumDeliveryDates);
        }
        private void put(Node sender, Node destination, int deliveryDate, Map<Node, Map<Node, Integer>> maximumDeliveryDates) {
            Map<Node, Integer> destinationsMap = getDestinationsFor(sender, maximumDeliveryDates);
            destinationsMap.put(destination, deliveryDate);
        }

        /**
         * <p>Clears existing records of delivery dates which predates the provided date. </p>
         * @param date the date of the first delivery date to keep.
         */
        public void clearUpTo(int date) {
            Map<Node, Map<Node, Integer>> newDeliveryDates = new HashMap<>();

            for (Map.Entry<Node, Map<Node, Integer>> nodeIntegerMap : maximumDeliveryDates.entrySet()) {
                Node sender = nodeIntegerMap.getKey();
                for (Map.Entry<Node, Integer> nodeIntegerEntry : nodeIntegerMap.getValue().entrySet()) {
                    int currentMax = nodeIntegerEntry.getValue();
                    if(stillValid(currentMax, date)) {
                        Node destination = nodeIntegerEntry.getKey();
                        put(sender, destination, currentMax, newDeliveryDates);
                    }
                }
            }

            maximumDeliveryDates = newDeliveryDates;
        }

        private boolean stillValid(int currentMax, int value) {
            return currentMax > value;
        }
    }
}
