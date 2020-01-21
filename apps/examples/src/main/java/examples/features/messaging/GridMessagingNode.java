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

package examples.features.messaging;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;


public class GridMessagingNode extends Node {

    private static int msgNumber = 0;

    private static int nbReceived = 0;
    private static int delaySum = 0;
    private boolean selected;

    @Override
    public void onStart() {
        setColor(null);
    }

    @Override
    public void onSelection() {
        setColor(Color.RED);
        sendAll(createMessage());
        selected = true;
    }

    @Override
    public void onClock() {
//
//        if(selected) {
//            sendAll(createMessage());
//            selected = false;
//        }
    }

    protected Message createMessage() {
        MessageContentTTL content = new MessageContentTTL();
        msgNumber++;
        System.out.println("["+ getID() + " - " + content.messageNumber+"] emitted at " + content.sendTime);
        return new Message(content);
    }

    @Override
    public void onMessage(Message message) {
        if (getColor()==null)
            getCommonLinkWith(message.getSender()).setWidth(4);
        setColor(Color.RED);

        MessageContentTTL content = (MessageContentTTL)message.getContent();
        logReceived(content);

        forwardIfNeeded(message, content);
    }

    private void logReceived(MessageContentTTL content) {
        int currentTime = getTime();
        int deliveryDuration = (currentTime - content.sendTime);
        int messageNumber = content.messageNumber;
        System.out.println("["+  getID() + "]  (" + messageNumber + ", " + content.ttl + ") received at "
                + currentTime + ", deliveryDuration = " + deliveryDuration + " rounds");

        delaySum += deliveryDuration;
        nbReceived++;

        System.out.println(String.format("Average delivery duration %.3f", (double)delaySum / nbReceived));
    }

    private void forwardIfNeeded(Message message, MessageContentTTL content) {
        if(!content.stillValid())
            return;

        MessageContentTTL forwardedContent = new MessageContentTTL(content);

        for (Node node : getNeighbors())
            if (node != message.getSender())
                send(node, new Message(forwardedContent));
    }

    @Override
    public String toString() {
        return "[" + getID() + "] " + nbReceived;
    }


    private class MessageContentTTL {
        protected static final int MAX_TTL = 10;
        int ttl = MAX_TTL -1 ;
        int messageNumber = msgNumber;
        int sendTime = getTime();

        public MessageContentTTL(MessageContentTTL content) {
            ttl = content.ttl - 1;
            messageNumber = content.messageNumber;
        }

        public MessageContentTTL() {
        }

        public boolean stillValid() {
            return ttl > 0;
        }
    }
}
