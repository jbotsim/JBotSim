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

package examples.centralized;

import io.jbotsim.contrib.algos.Algorithms;
import io.jbotsim.contrib.algos.Connectivity;
import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;

import java.util.List;


public class MST implements ClockListener {
	Topology tp;
	public MST(Topology tp){
		this.tp = tp;
		tp.addClockListener(this, 10);
	}
	protected void updateMST(){
		if (Connectivity.isConnected(tp)) {
            List<Link> mstLinks = Algorithms.getMST(tp);
            for (Link l : tp.getLinks())
                l.setWidth(mstLinks.contains(l) ? 5 : 0);
        }else{
            for (Link l : tp.getLinks())
                l.setWidth(1);
        }
	}
	public static void main(String[] args) {
		Topology tp = new Topology();
		new MST(tp);
		new JViewer(tp);
	}

    @Override
    public void onClock() {
        updateMST();
    }
}
