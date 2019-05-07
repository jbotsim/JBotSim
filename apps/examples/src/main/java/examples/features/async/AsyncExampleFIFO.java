package examples.features.async;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyLayouts;
import io.jbotsim.ui.JViewer;
import io.jbotsim.contrib.messaging.AsyncMessageEngine;
import io.jbotsim.gen.basic.TopologyGenerators;


public class AsyncExampleFIFO extends Node {

    int msg = 0;

    @Override
    public void onSelection() {
        sendMessage();
        msg++;
        sendMessage();
        msg++;
    }

    protected void sendMessage() {
        AsyncTestMessage content = new AsyncTestMessage();
        sendAll(new Message(content));
        System.out.println("["+content.messageNumber+"] emitted at " + content.sendTime);
    }

    @Override
    public void onMessage(Message message) {
        AsyncTestMessage asyncTestMessage = (AsyncTestMessage)message.getContent();
        int currentTime = getTime();
        int deliveryDuration = (currentTime - asyncTestMessage.sendTime);
        int messageNumber = asyncTestMessage.messageNumber;
        System.out.println("["+ messageNumber +"] received at " + currentTime + ", deliveryDuration = " + deliveryDuration + " rounds");
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setTimeUnit(20);
        AsyncMessageEngine messageEngine = new AsyncMessageEngine(tp);
        messageEngine.setAverageDuration(20);
        messageEngine.setDelay(20);
        messageEngine.setDebug(true);
        tp.setMessageEngine(messageEngine);
        tp.setDefaultNodeModel(AsyncExampleFIFO.class);
        TopologyGenerators.generateLine(tp, 2);
        new JViewer(tp);
        tp.start();
    }

    private class AsyncTestMessage {
        int messageNumber = msg;
        int sendTime = getTime();
    }
}
