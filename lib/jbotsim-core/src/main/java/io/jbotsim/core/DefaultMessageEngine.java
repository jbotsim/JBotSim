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

import io.jbotsim.core.event.MessageListener;

import java.util.*;

/**
 * <p>The {@link DefaultMessageEngine} is JBotSim's default {@link MessageEngine} implementation.</p>
 * <p>It is responsible for the regular transmission of the available {@link Message Messages} from any sender
 * {@link Node} of the {@link Topology} to their destination.</p>
 *
 * <p>{@link Message Messages} sent during round <em>n</em> are delivered at the beginning of round
 * <em>n+1</em>; provided that the corresponding arc (a {@link Link} going, at least, from the
 * {@link Message}'s source {@link Node} to its destination) actually exists at the beginning of round <em>n+1</em>.</p>
 *
 * <p>If a {@link Message} can't be delivered during one round, and {@link Message#isRetryModeEnabled()} returns
 * <code>true</code> (<em>i.e.</em> it has been sent with {@link Node#sendRetry(Node, Message)}), the
 * {@link DefaultMessageEngine} will keep re-queuing it until it is actually delivered.</p>
 */
public class DefaultMessageEngine implements MessageEngine {

    protected Topology topology;
    protected boolean debug = false;

    /**
     * <p>Creates a {@link DefaultMessageEngine}.</p>
     *
     * @param topology the {@link Topology} to use.
     */
    public DefaultMessageEngine(Topology topology) {
        this.topology = topology;
    }

    @Override
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    @Override
    public void onClock() {
        List<Node> nodes = topology.getNodes();

        clearMailboxes(nodes);

        List<Message> newMessages = collectMessages(nodes);
        removeIrrelevantMessages(newMessages.listIterator(), nodes);

        deliverMessages(newMessages);

    }

    /**
     * <p>Removes any irrelevant messages from the {@link ListIterator} according to the {@link Collection} of existing
     * {@link Node Nodes}.</p>
     * @param from the {@link ListIterator} from which irrelevant messages should be removed.
     * @param existingNodes the {@link Collection} of existing {@link Node Nodes}.
     */
    protected void removeIrrelevantMessages(ListIterator<Message> from, Collection<Node> existingNodes) {
        while(from.hasNext())
            removeNextMessageIfIrrelevant(from, existingNodes);
    }

    /**
     * <p>Removes the next messages from the {@link ListIterator} if it is irrelevant according to the {@link Collection}
     * of existing {@link Node Nodes}.</p>
     * @param from the {@link ListIterator} from which the next message should be inspected.
     * @param existingNodes the {@link Collection} of existing {@link Node Nodes}.
     */
    protected void removeNextMessageIfIrrelevant(ListIterator<Message> from, Collection<Node> existingNodes) {
        Message next = from.next();
        if(!isMessageStillRelevant(next)) {
            requeueIfNeeded(next, existingNodes);
            from.remove();
        }
    }

    /**
     * <p>Clears the mailboxes of all {@link Node Nodes} present in the topology.</p>
     * @see #clearMailboxes(Collection)
     */
    protected void clearMailboxes() {
        clearMailboxes(topology.getNodes());
    }

    /**
     * <p>Clears the mailboxes of the specified {@link Collection} of {@link Node Nodes}.</p>
     * @param nodes a {@link Collection} of {@link Node} which mailboxes should be cleared.
     * @see Node#getMailbox()
     */
    protected void clearMailboxes(Collection<Node> nodes) {
        for (Node node : nodes)
            node.getMailbox().clear();
    }

    /**
     * <p>Collects outgoing {@link Message Messages} from all {@link Node Nodes} present in the topology.</p>
     * @see #collectMessages(Collection)
     * @return the {@link List} of {@link Message Messages} that have been collected.
     */
    protected List<Message> collectMessages() {
        return collectMessages(topology.getNodes());
    }

    /**
     * <p>Collects outgoing {@link Message Messages} from the specified {@link Collection} of {@link Node Nodes}.</p>
     * @param nodes a {@link Collection} of {@link Node} whose outgoing messages should be collected.
     * @return the {@link List} of {@link Message Messages} that have been collected.
     */
    protected List<Message> collectMessages(Collection<Node> nodes) {
        List<Message> messages = new ArrayList<>();
        for (Node n : nodes)
            collectMessages(messages, n);

        return messages;
    }

    /**
     * <p>Collects outgoing {@link Message Messages} from the specified {@link Node}.</p>
     *
     * <p>A message with a <code>null</code> destination will be duplicated for each neighbor of the send.</p>
     *
     * @param newMessages a {@link Collection} of {@link Message Messages} in which outgoing messages should be added.
     * @param node the {@link Node} whose outgoing messages should be collected.
     *
     * @see Node#getOutNeighbors()
     */
    protected void collectMessages(Collection<Message> newMessages, Node node) {
        for (Message message : node.sendQueue)
            if(message.getDestination() == null)
                for (Node outNeighbor : node.getOutNeighbors())
                    newMessages.add(message.withDestination(outNeighbor));
            else
                newMessages.add(message);

        node.sendQueue.clear();
    }

    /**
     * <p>Tests whether the provided {@link Message} is still relevant.</p>
     * <p>To be relevant, an arc must exist between the sender and the destination of the message.</p>
     * @param message the {@link Message} to be tested.
     * @return <code>true</code> if an arc exists between the send and the destination of the message.
     * @see Node#getOutNeighbors()
     * @see Message#getSender()
     * @see Message#getDestination()
     */
    protected boolean isMessageStillRelevant(Message message) {
        return message.getSender().hasOutNeighbor(message.getDestination());
    }

    /**
     * <p>Re-queues the specified {@link Message} if necessary</p>
     * <p>For a {@link Message} to be re-queueable, the following criteria must be met:</p>
     * <ul>
     *     <li>the message's retry mode must be enabled</li>
     *     <li>the message's sender must still exist</li>
     *     <li>the message's destination must still exist</li>
     * </ul>
     * @param message the {@link Message} which should be re-queued.
     * @param existingNodes the {@link Collection} of existing {@link Node Nodes}.
     * @see Message#isRetryModeEnabled()
     * @see Message#getSender()
     * @see Message#getDestination()
     */
    protected void requeueIfNeeded(Message message, Collection<Node> existingNodes) {
        if (!message.isRetryModeEnabled())
            return;
        if (!existingNodes.contains(message.getSender()))
            return;
        if (!existingNodes.contains(message.getDestination()))
            return;

        requeueMessage(message);
    }

    /**
     * <p>Re-queues the specified {@link Message} in its sender's send queue.</p>
     * @param message the {@link Message} which should be re-queued.
     * @return <code>true</code> if the re-queue has been successful.
     */
    protected boolean requeueMessage(Message message) {
        return message.getSender().sendQueue.add(message);
    }

    /**
     * <p>Delivers the provided {@link Message Messages} to send.</p>
     * @param messagesToSend a {@link Collection} of {@link Message Messages} to deliver.
     * @see #deliverMessage(Message)
     */
    protected void deliverMessages(Collection<Message> messagesToSend) {
        for (Message message : messagesToSend)
            deliverMessage(message);
    }

    /**
     * <p>Delivers the {@link Message} to its destination.</p>
     * @param message the {@link Message} to be delivered.
     */
    protected void deliverMessage(Message message) {
        message.getDestination().getMailbox().add(message);
        message.getDestination().onMessage(message);
        for (MessageListener ml : topology.messageListeners)
            ml.onMessage(message);
        if (debug)
            System.err.println(topology.getTime() + ": " + message);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * <p>Resets the {@link DefaultMessageEngine}.</p>
     * <ul>
     *   <li>Any {@link Message} (ready to be sent or ready to be received) handled by the
     * {@link DefaultMessageEngine} is discarded.</li>
     *   <li>Other configurations (debug, topology) remain untouched.</li>
     * </ul>
     */
    @Override
    public void reset() {
        List<Node> nodes = topology.getNodes();
        clearMailboxes(nodes);
        clearSendQueues(nodes);
    }

    /**
     * <p>Clears the send queue of all {@link Node Nodes} present in the topology.</p>
     * @see #clearSendQueues(Collection)
     */
    protected void clearSendQueues() {
        clearSendQueues(topology.getNodes());
    }

    /**
     * <p>Clears the send queue of all {@link Node Nodes} present in the topology.</p>
     * @param nodes a {@link Collection} of {@link Node} whose send queues should be cleared.
     */
    protected void clearSendQueues(Collection<Node> nodes) {
        for (Node node: nodes)
            node.sendQueue.clear();
    }

}
