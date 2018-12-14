package examples.features.async;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.messaging.AsyncMessageEngine;
import io.jbotsim.topology.TopologyGenerator;

import io.jbotsim.core.Color;

/**
 * Created by acasteig on 9/18/15.
 */
public class AsyncExampleGrid extends Node {

    @Override
    public void onStart() {
        setColor(null);
    }

    @Override
    public void onSelection() {
        setColor(Color.red);
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message message) {
        if (getColor()==null)
            getCommonLinkWith(message.getSender()).setWidth(4);
        setColor(Color.red);
        for (Node node : getNeighbors())
            if (node != message.getSender())
                send(node, message);
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setClockSpeed(200);
        tp.setDefaultNodeModel(AsyncExampleGrid.class);
        tp.setMessageEngine(new AsyncMessageEngine(20, AsyncMessageEngine.Type.FIFO));
        TopologyGenerator.generateGrid(tp, 5, 4);
        new JViewer(tp);
    }
}
