package examples.fancy.canadairs;

import jbotsim.Message;
import jbotsim.Node;

import jbotsim.Point;
import java.util.Stack;

public class Canadair extends Node {
    Point target;
    Point parking;
    Node station = null;
    Stack<Point> trajet = new Stack<Point>();
    Point lake = new Point(50,50);

    public Canadair(){
        setIcon("/examples/fancy/canadairs/canadair.png");
        setSize(18);
        setCommunicationRange(120);
        setSensingRange(30);
    }

    @Override
    public void onStart() {
        target = parking = getLocation();
    }

    @Override
    public void onClock() {
        advance();

        if (target == parking) {
            if (station != null) {
                send(station, new Message("ALIVE"));
            } else {
                sendAll(new Message("ALIVE"));
            }
        }
    }

    private void onArrival(){
        if (!trajet.empty()){
            setTarget(trajet.pop());
        } else if (target != parking){
            this.setTarget(parking);
        }
    }

    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Fire){
            ((Fire) node).die();
        }
    }

    // Gardez cette méthode telle quelle
    private void setTarget(Point target){
        setDirection(target);
        this.target = target;
    }

    @Override
    public void onMessage(Message message) {
        if (message.getContent() instanceof Point &&
                message.getSender() instanceof Station &&
                trajet.isEmpty()){
            Point targetSensor = (Point) message.getContent();

            Point t1 = new Point(targetSensor.getX() - (getSensingRange()),
                    targetSensor.getY() + (getSensingRange()));
            Point t2 = new Point(targetSensor.getX() - (getSensingRange()),
                    targetSensor.getY() - (getSensingRange()));
            Point t3 = new Point(targetSensor.getX() + (getSensingRange()),
                    targetSensor.getY() - (getSensingRange()));
            Point t4 = new Point(targetSensor.getX() + (getSensingRange()),
                    targetSensor.getY() + (getSensingRange()));

            trajet.push(t1);
            trajet.push(t2);
            trajet.push(t3);
            trajet.push(t4);
            trajet.push(t1);

            trajet.push(lake);
            setTarget(trajet.pop());

            station = message.getSender();
        }
    }

    // Gardez cette méthode telle quelle
    private void advance(){
        if (target != parking || ! getLocation().equals(parking))
            move(2);
        if (distance(target) < 2) {
            setLocation(target);
            onArrival();
        }
    }
}
