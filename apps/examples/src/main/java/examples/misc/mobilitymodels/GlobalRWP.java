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

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;

public class GlobalRWP implements ClockListener{
	Topology tp;
	public GlobalRWP (Topology tp){
		this.tp = tp;
		tp.addClockListener(this);
	}
	public void onClock(){
		for (Node n : tp.getNodes()){
			Point target = (Point)n.getProperty("target");
			if (target == null || n.getLocation().distance(target) < 2){
				target = new Point(Math.random()*400, Math.random()*300);
				n.setProperty("target", target);
			}
			n.setDirection(target);
			n.move();
		}
	}
	public static void main(String[] args){
		Topology tp = new Topology(400,300);
		new GlobalRWP(tp);
		new JViewer(tp);
		tp.start();
	}
}
