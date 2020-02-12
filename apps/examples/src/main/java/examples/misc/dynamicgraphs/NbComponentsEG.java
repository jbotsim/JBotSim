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
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.contrib.algos.Connectivity;
import io.jbotsim.gen.dynamic.graph.EMEGPlayer;
import io.jbotsim.gen.dynamic.graph.TVG;


public class NbComponentsEG implements ClockListener{

	static Topology tp = new Topology();
	
	@Override
	public void onClock() {
		// prints the number of components in the graph
		System.out.println(Connectivity.splitIntoConnectedSets(tp.getNodes()).size());
	}

	public static void main(String[] args) {
		new JViewer(tp);
		TVG tvg=new TVG(Node.class); tvg.buildCompleteGraph(6);
		(new EMEGPlayer(tvg, tp, .05, .6)).start();
		tp.addClockListener(new NbComponentsEG());
		tp.start();
	}
}
