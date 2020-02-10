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

import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

import java.util.ArrayList;
import java.util.Random;

/* Gardez cette classe telle quelle */
public class Fire extends Node {
    static ArrayList<Fire> allFires = new ArrayList<Fire>();
    Random r=new Random();

    @Override
	public void onStart(){
        disableWireless();
        allFires.add(this);
        setIcon(Icons.FIRE);
        setIconSize(10);
	}
    @Override
	public void onClock(){
        if (Math.random() < 0.01)
            propagate();
	}
    public void propagate(){
        if (allFires.size() < 100) {
            double x = getX() + r.nextDouble() * 20 - 10;
            double y = getY() + r.nextDouble() * 20 - 10;
            for (Fire fire : allFires)
                if (fire.distance(x, y) < 10)
                    return;
            getTopology().addNode(x, y, new Fire());
        }
    }

    @Override
    public void die(){
        super.die();
        allFires.remove(this);
    }
}
