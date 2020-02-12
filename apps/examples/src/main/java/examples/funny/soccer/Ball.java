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

package examples.funny.soccer;

import io.jbotsim.core.Node;

import io.jbotsim.core.Point;

/**
 * Created by Arnaud Casteigts on 06/04/17.
 */
public class Ball extends Node{
    double speed = 0;
    static final double fadingRatio = 1.2;

    @Override
    public void onStart() {
        setIcon("/examples/funny/soccer/ball.png");
        disableWireless();
    }

    @Override
    public void onClock() {
        if (speed > 0) {
            move(speed);
            speed = speed / fadingRatio;
            wrapLocation();
            if (speed < 1) {
                speed = 0;
            }
        }
    }

    public void randomShoot(){
        double x = Math.random()*getTopology().getWidth();
        double y = Math.random()*getTopology().getHeight();
        Point p = new Point(x, y);
        double speed = Math.random()*40 + 10;
        shoot(p, speed);
    }

    public void shoot(double angle, double speed){
        setDirection(angle);
        this.speed = speed;
    }

    public void shoot(Point direction, double speed){
        setDirection(direction);
        this.speed = speed;
    }
}
