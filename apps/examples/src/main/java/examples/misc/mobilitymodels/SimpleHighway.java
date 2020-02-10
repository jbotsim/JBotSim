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
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.TopologyListener;
import io.jbotsim.ui.JViewer;

import java.util.Random;


public class SimpleHighway implements ClockListener, TopologyListener{
	Topology tp;
    Random rand=new Random();
	boolean voie=true;
    
	public SimpleHighway(Topology tp){
		this.tp=tp;
		tp.addTopologyListener(this);
		tp.addClockListener(this);
	}

	public void onNodeAdded(Node n) {
		n.setProperty("speed", new Double(rand.nextDouble()*50+30));
		n.setLocation(n.getX(),voie?200:186);
		n.setDirection(0);
        voie=!voie;
	}
	public void onClock() {
		for (Node n : tp.getNodes()){
			n.move((Double)n.getProperty("speed")/16.0);
			if (n.getX()>800)
				n.setLocation(0, n.getY());
		}
	}
	public void onNodeRemoved(Node n) {}
	
	public static void main(String args[]){
		Topology tp=new Topology();
		new SimpleHighway(tp);
		new JViewer(tp);
		tp.start();
	}
}