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

package examples.funny.wolfsheep;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.icons.Icons;

/**
 * Created by acasteig on 31/08/16.
 */
public class Sheep extends Node {
    private int speed = 1;
    private boolean isAlive = true;

    @Override
    public void onStart() {
        setIcon(Icons.SHEEP);
        setIconSize(12);
        setDirection(Math.random() * Math.PI * 2);
    }

    @Override
    public void onClock() {
        move(speed);
        wrapLocation();
    }

    @Override
    public void onPostClock() {
        Topology tp = getTopology();
        if ( ! isAlive ) {
            tp.removeNode(this);
            if (Math.random() < 0.5){
                tp.addNode(-1, -1, new Wolf());
            }
        }else{
            if (Math.random() < 0.01){
                tp.addNode(-1, -1, new Sheep());
            }
        }
    }

    public void kill(){
        isAlive = false;
    }
}
