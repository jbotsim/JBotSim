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

package examples.funny.cowboy;

import io.jbotsim.core.Node;

public class Cow extends Node{
    static Node farmer;
    int speed = 0;

    public Cow(){
        setIcon("/examples/funny/cowboy/cow.png");
        setIconSize(30);
        disableWireless();
    }

    @Override
    public void onClock() {
        if (speed > 0)
            move(speed--);
        else
            if (speed == 0 && this.distance(farmer)<20) {
                setDirection(farmer.getLocation(),true);
                speed = 12;
            }
        wrapLocation();
    }
}
