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

import java.util.*;

/**
 * Created by acasteig on 22/03/15.
 */
public class Station extends Node {

    Map<Canadair, Integer> canadairs = new HashMap<Canadair, Integer>();
    Map<Canadair, Point> destinationByCanadair = new HashMap<Canadair, Point>();
    Map<Point, Canadair> canadairByDestination = new HashMap<Point, Canadair>();

    public Station() {
        setIcon(Icons.STATION);
        setIconSize(25);
        setCommunicationRange(120);
    }

    @Override
    public void onClock() {
        super.onClock();

        List<Canadair> toRemove = new ArrayList<Canadair>();

        for (Map.Entry<Canadair, Integer> e : canadairs.entrySet()){
            if (getTime() - e.getValue() > 10){
                toRemove.add(e.getKey());
            }
        }

        for (Canadair c : toRemove){
            canadairs.remove(c);
        }
         toRemove.clear();
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);

        if (message.getContent() instanceof Point){

            Point target = (Point)message.getContent();

            if (!canadairByDestination.containsKey((target))){
                Iterator it = canadairs.entrySet().iterator();
                boolean found = false;
                Canadair choosenOne = null;
                while (it.hasNext() && !found) {
                    Map.Entry pair = (Map.Entry)it.next();
                    if (!destinationByCanadair.containsKey((Canadair)pair.getKey())){
                        found = true;
                        choosenOne = (Canadair) pair.getKey();
                    }
                }
                // Si un canadaire disponible
                if (choosenOne != null){
                    send(choosenOne, message);
                    canadairByDestination.put(target, choosenOne);
                    destinationByCanadair.put(choosenOne, target);
                }
            }

        }

        if (message.getContent() == "ALIVE"
                && message.getSender() instanceof Canadair
                && !canadairs.containsKey(message.getSender())){
            canadairs.put((Canadair)(message.getSender()), getTime());
            if(destinationByCanadair.containsKey(message.getSender())){
                Point dest = destinationByCanadair.get(message.getSender());
                destinationByCanadair.remove(message.getSender());
                canadairByDestination.remove(dest);
            }

        }
    }

    @Override
    public void onStart() {
        sendAll(new Message());
    }
}
