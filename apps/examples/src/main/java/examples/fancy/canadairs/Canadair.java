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

package examples.fancy.canadairs;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

import java.util.Stack;

public class Canadair extends Node {
    Point target;
    Point parking;
    Node station = null;
    Stack<Point> trajet = new Stack<Point>();
    Point lake = new Point(50,50);

    public Canadair(){
        setIcon(Icons.CANADAIR);
        setIconSize(18);
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
