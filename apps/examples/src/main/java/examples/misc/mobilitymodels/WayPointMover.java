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

package examples.misc.mobilitymodels;

import io.jbotsim.core.Node;
import io.jbotsim.core.event.ClockListener;

import io.jbotsim.core.Point;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by acasteig on 14/06/15.
 */
public class WayPointMover implements ClockListener{
    Node node;
    Queue<Point> destinations = new LinkedList<Point>();
    ArrivalListener listener;

    public WayPointMover(Node node) {
        this.node = node;
        node.getTopology().addClockListener(this);
    }

    public void addArrivalListener(ArrivalListener listener){
        this.listener = listener;
    }

    public void addDestination(double x, double y){
        destinations.add(new Point(x, y));
    }

    @Override
    public void onClock() {
        if (destinations.size() > 0) {
            Point dest = destinations.peek();
            if (node.distance(dest) >= 1) {
                node.setDirection(dest);
                node.move();
            }else {
                listener.onArrival();
                destinations.remove();
            }
        }
    }
}
