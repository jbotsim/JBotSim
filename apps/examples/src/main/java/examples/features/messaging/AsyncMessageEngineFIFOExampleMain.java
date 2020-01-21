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

package examples.features.messaging;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.contrib.messaging.AsyncMessageEngine;
import io.jbotsim.gen.basic.TopologyGenerators;


public class AsyncMessageEngineFIFOExampleMain extends Node {

    static int msg = 0;

    @Override
    public void onSelection() {
        int nbMsg = 50;
        for(int i = 0; i < nbMsg; i++)
            sendMessage();
    }

    protected void sendMessage() {
        MessageContent content = new MessageContent();
        sendAll(new Message(content));
        System.out.println("["+ getID() + " - " + content.messageNumber + "] emitted at " + content.sendTime);
        msg++;
    }

    @Override
    public void onMessage(Message message) {
        MessageContent messageContent = (MessageContent)message.getContent();
        int currentTime = getTime();
        int deliveryDuration = (currentTime - messageContent.sendTime);
        int messageNumber = messageContent.messageNumber;
        System.out.println("["+ messageNumber +"] received at " + currentTime + ", deliveryDuration = " + deliveryDuration + " rounds");
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setTimeUnit(20);
        AsyncMessageEngine messageEngine = new AsyncMessageEngine(tp);
        messageEngine.setAverageDuration(20);
//        messageEngine.setDebug(true);
        tp.setMessageEngine(messageEngine);
        tp.setDefaultNodeModel(AsyncMessageEngineFIFOExampleMain.class);
        TopologyGenerators.generateLine(tp, 2);
        new JViewer(tp);
        tp.start();
    }

    private class MessageContent {
        int messageNumber = msg;
        int sendTime = getTime();
    }
}
