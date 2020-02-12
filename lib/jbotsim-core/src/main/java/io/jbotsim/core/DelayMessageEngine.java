/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

import java.util.*;

/**
 * <p>The {@link DelayMessageEngine} is responsible for the regular transmission of the available
 * {@link Message Messages} from any sender {@link Node} of the {@link Topology} to their destination.</p>
 *
 * <p>By default, {@link Message Messages} sent during round <em>n</em> are delivered at the beginning of round
 * <em>n+1</em>; provided that the corresponding arc (a {@link Link} going, at least, from the
 * {@link Message}'s source {@link Node} to its destination) actually exists at the beginning of round <em>n+1</em>.</p>
 *
 * <h3>Delay feature</h3>
 * <p>The previously explained default delivery duration ({@link #DEFAULT_DELAY}) is said <em>instantaneous</em>
 * ({@link #DELAY_INSTANT}).
 * The {@link DelayMessageEngine} allows you to modify this duration by specifying the
 * amount of rounds {@link Message Messages} should take to be delivered, using {@link #setDelay(int)}.</p>
 *
 * <h3><code>Link</code> checks</h3>
 * <p>By default, each round,  the {@link DelayMessageEngine} checks for each {@link Message}
 * that the corresponding {@link Link} is still present. If not, the {@link Message} is dropped.</p>
 * <p>The time spent doing this rises with the delay and the number of {@link Node Nodes} and {@link Message Messages}.
 * Depending on your case, you might want to disable this using {@link #disableLinksContinuityChecks()}.</p>
 */
public class DelayMessageEngine extends DefaultMessageEngine {

    /**
     * The delay value to use for the shortest delivery time possible; value: {@value #DELAY_INSTANT}.
     * @see #setDelay(int)
     * @see #getDelay()
     */
    public static final int DELAY_INSTANT = 1;

    /**
     * The default number of round before message delivery; value: {@value #DELAY_INSTANT}.
     * @see #setDelay(int)
     * @see #getDelay()
     */
    public static final int DEFAULT_DELAY = DELAY_INSTANT;
    private int delay;

    protected Map<Integer, List<Message>> delayedMessages = new HashMap<>();

    protected int currentTime;
    private boolean shouldCheckLinksContinuity = true;

    /**
     * <p>Creates a {@link DelayMessageEngine}.</p>
     * <p>By default, the messages sent during one round are actually during the next.</p>
     *
     * @param topology the {@link Topology} to use.
     */
    public DelayMessageEngine(Topology topology) {
        this(topology, DEFAULT_DELAY);
    }

    /**
     * <p>Creates a {@link DelayMessageEngine}.</p>
     *
     * @param topology the {@link Topology} to use.
     * @param delay the number of round a message should be delayed, as an integer.
     */
    public DelayMessageEngine(Topology topology, int delay) {
        super(topology);

        assert(delay >= 0);
        setDelay(delay);

        this.shouldCheckLinksContinuity = true;
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
     * @see #getDelay()
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
     * @see #setDelay(int)
     */
    public int getDelay() {
        return delay;
    }

    /**
     * <p>Specifies whether a {@link Message} should be removed if the corresponding {@link Link} disappears at some
     * point during its waiting delay.</p>
     *
     * <p><b>Note:</b> Whatever the status, the link presence will be checked at sending time and delivery time.</p>
     *
     * @return <code>true</code> if intermediate checks and removal should be performed; <code>false</code> otherwise.
     * @see #disableLinksContinuityChecks()
     */
    public boolean shouldCheckLinksContinuity() {
        return shouldCheckLinksContinuity;
    }

    /**
     * <p>Disables the intermediate checks on {@link Link} existence for each {@link Message}.</p>
     * @see #shouldCheckLinksContinuity()
     */
    public void disableLinksContinuityChecks() {
        this.shouldCheckLinksContinuity = false;
    }

    @Override
    public void onClock() {
        currentTime = topology.getTime();

        List<Node> nodes = topology.getNodes();

        clearMailboxes(nodes);

        List<Message> newMessages = collectMessages(nodes);
        removeIrrelevantMessages(newMessages.listIterator(), nodes);

        List<Message> messagesToSend = getMessagesToSend(newMessages, nodes);
        deliverMessages(messagesToSend);

        delayedMessages.remove(currentTime);
    }

    /**
     * <p>Constructs the {@link List} of {@link Message Messages} that must be sent during this round.</p>
     * @param newMessages the {@link List} of new {@link Message Messages} which has been collected during this round.
     * @param existingNodes the {@link Collection} of existing {@link Node Nodes}.
     * @return the {@link List} of {@link Message Messages} that must be sent during this round.
     */
    protected List<Message> getMessagesToSend(List<Message> newMessages, Collection<Node> existingNodes) {
        List<Message> currentDateMessages;

        if (noCachingNeeded(newMessages))
            currentDateMessages = newMessages;
        else {
            if(shouldCheckLinksContinuity())
                removeIrrelevantMessages(existingNodes);

            cacheNewMessages(newMessages);
            currentDateMessages = getMessagesForCurrentDate();

            if(!shouldCheckLinksContinuity())
                removeIrrelevantMessages(currentDateMessages.listIterator(), existingNodes);
        }
        return currentDateMessages;
    }

    /**
     * <p>Tests whether the provided list of {@link Message Messages} should be cached or not.</p>
     * @param newMessages a {@link List} containing new {@link Message Messages}.
     * @return <code>true</code> if the provided messages should not be cached.
     */
    protected boolean noCachingNeeded(List<Message> newMessages) {
        return getDelay() == DELAY_INSTANT && delayedMessages.isEmpty();
    }

    /**
     * <p>Removes any irrelevant messages from the cached delayed messages, according to the {@link Collection} of
     * existing {@link Node Nodes}.</p>
     * @param existingNodes the {@link Collection} of existing {@link Node Nodes}.
     * @see #removeIrrelevantMessages(ListIterator, Collection)
     */
    protected void removeIrrelevantMessages(Collection<Node> existingNodes) {
        for (List<Message> messageList : delayedMessages.values())
            removeIrrelevantMessages(messageList.listIterator(), existingNodes);
    }

    /**
     * <p>Caches the provided {@link List} of new {@link Message Messages}.</p>
     * @param messages a {@link List} containing new {@link Message Messages}.
     * @see #prepareNewMessagesForCaching(List)
     * @see #cacheMessagesAtTime(List, int)
     */
    protected void cacheNewMessages(List<Message> messages) {

        Map<Integer, List<Message>> newMessages = prepareNewMessagesForCaching(messages);

        for (Map.Entry<Integer, List<Message>> entry : newMessages.entrySet())
            cacheMessagesAtTime(entry.getValue(), entry.getKey());

    }

    /**
     * <p>Transforms the provided {@link List} of new {@link Message Messages} for caching into a suitable data
     * structure.</p>
     * @param newMessages a {@link List} containing new {@link Message Messages}.
     * @return a {@link Map} containing {@link List Lists} of {@link Message Messages} indexed by the date at which they
     * should be delivered.
     * @see #getCurrentDeliveryDate()
     */
    protected Map<Integer, List<Message>> prepareNewMessagesForCaching(List<Message> newMessages) {
        Map<Integer,List<Message>> messagesMap = new HashMap<>();

        messagesMap.put(getCurrentDeliveryDate(), newMessages);

        return messagesMap;
    }

    /**
     * <p>Caches a specific {@link Message} at it's planned delivery time (round number).</p>
     * @param messagesMap the {@link Map}, indexing {@link Message Messages} by their delivery date, in which the
     * message should be cached.
     * @param message the {@link Message} to cache.
     * @param deliveryTime the round number at which the message should be delivered.
     */
    protected void cacheMessageAtTime(Map<Integer,List<Message>> messagesMap, Message message, int deliveryTime) {
        if(messagesMap.containsKey(deliveryTime))
            messagesMap.get(deliveryTime).add(message);
        else {
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            messagesMap.put(deliveryTime, messageList);
        }
    }

    /**
     * <p>Caches (internally) the specified {@link List} of {@link Message Messages} at that the given delivery time
     * (round number).</p>
     * @param messages the {@link List} of {@link Message Messages} to cache internally.
     * @param deliveryTime the round number at which the messages should be delivered.
     */
    protected void cacheMessagesAtTime(List<Message> messages, int deliveryTime) {
        if(delayedMessages.containsKey(deliveryTime))
            delayedMessages.get(deliveryTime).addAll(messages);
        else
            delayedMessages.put(deliveryTime, messages);
    }

    /**
     * <p>Retrieves the list of {@link Message Messages} which should be delivered during the current round, from the
     * internal storage.</p>
     * @return a {@link List} of {@link Message Messages} containing all messages which should be delivered during the
     * current round. Can be empty, but not null.
     */
    protected List<Message> getMessagesForCurrentDate() {
        List<Message> messages = delayedMessages.get(currentTime);
        return messages != null ? messages : new ArrayList<>();
    }

    /**
     * <p>Computes the delay which should be applied to the provided {@link Message}.</p>
     * @param message the {@link Message} needing a delay.
     * @return the delay for the provided message.
     */
    protected int getDelayForMessage(Message message) {
        return getDelay();
    }

    /**
     * <p>Computes the delivery date (round number) for a {@link Message} collected at the start of the current
     * round.</p>
     * @return the delivery date for the current round, as an integer.
     */
    protected int getCurrentDeliveryDate() {
        return currentTime + getDelay() - 1;
    }

    /**
     * <p>Resets the {@link DelayMessageEngine}.</p>
     * <ul>
     *   <li>Any {@link Message} (ready to be sent, "in the air" or ready to be received) handled by the
     * {@link DelayMessageEngine} is discarded.</li>
     *   <li>Other configurations (delay or debug) remain untouched.</li>
     * </ul>
     */
    @Override
    public void reset() {
        super.reset();
        delayedMessages.clear();
    }


}
