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

import io.jbotsim.core.Point;
import java.util.Random;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class DavidNode extends Node{

	Point vec;

    @Override
    public void onStart() {
		vec = new Point(0.0,0.0);
    }

    public void onClock() {
		double randx = ((new Random()).nextInt(3)-1)/10.0;
		double randy = ((new Random()).nextInt(3)-1)/10.0;

		vec.setLocation(vec.getX()+randx, vec.getY()+randy);
        Point next = new Point(getX()+vec.getX(),getY()+vec.getY());
		setLocation(next);
		wrapLocation();
	}
	public static void main(String args[]){
		Topology tp = new Topology();
		tp.setDefaultNodeModel(DavidNode.class);
		new JViewer(tp);
		tp.start();
	}
}
