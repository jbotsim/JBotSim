package examples.fancy.canadairs;

import jbotsim.Message;
import jbotsim.Node;

import jbotsim.Color;
import jbotsim.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acasteig on 22/03/15.
 */
public class Sensor extends Node {
    Node parent;
    List<Fire> sensedFires;
    int lastReceivedTime;

    public Sensor() {
        setIcon("/examples/fancy/canadairs/sensor.png");
        setSize(16);
        setCommunicationRange(120);
        setSensingRange(60);
    }

    @Override
    public void onStart() {
        parent = null;
        lastReceivedTime = 0;
        sensedFires = new ArrayList<Fire>();
    }

    @Override
    public void onSensingOut(Node node) {
        if (node instanceof Fire){
            sensedFires.remove((Fire)node);
        }

        if (sensedFires.isEmpty()){
            this.setColor(null);
        }

    }

    @Override
    public void onClock() {
        super.onClock();

        if (!sensedFires.isEmpty()){
            send(parent, new Message(this.getLocation()));
        }

        if (getTime() - lastReceivedTime >= 10 && this.getColor() != Color.red){
            this.setColor(null);
        }
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        if (parent == null){
            parent = message.getSender();
            getCommonLinkWith(parent).setWidth(3);
            getCommonLinkWith(parent).setColor(Color.yellow);
            sendAll(message);
        }

        if (message.getContent() instanceof Point && message.getSender() instanceof Sensor){
            if(sensedFires.isEmpty())
                this.setColor(Color.orange);
            send(parent, message);
            lastReceivedTime = getTime();
        }
    }

    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Fire){
            this.setColor(Color.red);
            sensedFires.add((Fire)node);
        }
    }
}
