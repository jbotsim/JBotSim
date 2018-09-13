package examples.features.async;

import jbotsim.Message;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.messaging.AsyncMessageEngine;
import jbotsimx.topology.TopologyGenerator;

/**
 * Created by acasteig on 9/18/15.
 */
public class AsyncExampleFIFO extends Node {

    int msg = 0;

    @Override
    public void onSelection() {
        sendAll(new Message(msg));
        msg++;
        sendAll(new Message(msg));
        msg++;
        System.out.println("émis à " + getTime());
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("reçu à " + getTime());
        System.out.println(message.getContent());

        //double d = Math.random();
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setClockSpeed(100);
        tp.setMessageEngine(new AsyncMessageEngine(20, AsyncMessageEngine.Type.FIFO));
        //tp.getMessageEngine().setSpeed(10);
        tp.setDefaultNodeModel(AsyncExampleFIFO.class);
        TopologyGenerator.generateLine(tp, 2);
        new JViewer(tp);
    }
}
