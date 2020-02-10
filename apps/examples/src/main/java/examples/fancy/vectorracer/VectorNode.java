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

package examples.fancy.vectorracer;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

public abstract class VectorNode extends Node {
    public static final double DEVIATION = 10.0;
    public Point vector = new Point(0, 0);
    private Point nextPoint;
    private double speed;

    public void travelTo(Point requestedTarget) {
        Point projection = add(getLocation(), vector);
        Point requestedChange = sub(requestedTarget, projection);
        if (len(requestedChange) < DEVIATION) {
            nextPoint = requestedTarget;
        }else{
            Point allowedChange = mul(requestedChange, DEVIATION / len(requestedChange));
            nextPoint = add(projection, allowedChange);
        }
        setDirection(nextPoint);
        vector = sub(nextPoint, getLocation());
        speed = len(vector)/10.0; // 10 rounds for the vector (visualization smoothness)
    }

    @Override
    public void onClock() {
        if (nextPoint != null){
            if (distance(nextPoint) > speed) {
                move(speed);
            }else{
                setLocation(nextPoint);
                nextPoint = null;
                onPointReached(getLocation());
            }
        }
    }

    /* Called when a nextPoint is reached (to be overridden) */
    public abstract void onPointReached(Point point);

    /* Vector operations */
    private static double len(Point p) {
        return (new Point(0,0)).distance(p);
    }
    private static Point mul(Point p, double a) {
        return (new Point(p.getX()*a, p.getY()*a));
    }
    private static Point sub(Point p1, Point p2) {
        return (new Point(p1.getX()-p2.getX(), p1.getY()-p2.getY()));
    }
    private static Point add(Point p1, Point p2) {
        return (new Point(p1.getX()+p2.getX(), p1.getY()+p2.getY()));
    }
}
