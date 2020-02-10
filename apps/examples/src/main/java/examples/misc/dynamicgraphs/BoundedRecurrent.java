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

package examples.misc.dynamicgraphs;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.SelectionListener;

import io.jbotsim.core.Color;
import io.jbotsim.ui.JViewer;

import java.util.Random;

public class BoundedRecurrent implements ClockListener, SelectionListener {
	Topology tp;
	Random r=new Random();

	public BoundedRecurrent(Topology tp){
		this.tp = tp;
		tp.setTimeUnit(100);
		tp.addClockListener(this, 1);
		tp.addSelectionListener(this);
	}
	public void onClock() {
		for (Link l : tp.getLinks()){
			if (l.getWidth()>0){
				l.setWidth(0);
				l.setProperty("nextAppearance", tp.getTime()+r.nextInt(10)+1);
			}else{
				int nextAppearance = (Integer)l.getProperty("nextAppearance");
				if (tp.getTime() == nextAppearance){
					l.setWidth(2);
					doSomething(l);
				}
			}
		}
	}
	public void doSomething(Link l){
		if (l.endpoint(0).getColor()==Color.red && l.endpoint(1).getColor()!=Color.red)
			l.endpoint(1).setColor(Color.red);
		if (l.endpoint(1).getColor()==Color.red && l.endpoint(0).getColor()!=Color.red)
			l.endpoint(0).setColor(Color.red);
	}
	public static void main(String args[]){
		Topology tp=new Topology();
		new BoundedRecurrent(tp);
		new JViewer(tp);
		tp.start();
	}

	@Override
	public void onSelection(Node node) {
		node.setColor(Color.red);
	}
}