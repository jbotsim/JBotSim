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

package examples.centralized.grapheS4;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.ui.JViewer;

import io.jbotsim.core.Color;

public class DFS implements SelectionListener{
	Topology tp;
	
	public DFS(Topology tp){
		this.tp=tp;
		tp.addSelectionListener(this);
	}

	private void buildFrom(Node node){
		node.setColor(Color.black);
		for (Node ng : node.getNeighbors()){
			if (ng.getColor() != Color.black){
				ng.setColor(Color.black);
				node.getCommonLinkWith(ng).setWidth(3);
			}
		}
	}
	
	private void reset(){
		for (Node node : tp.getNodes())
			node.setColor(null);	
		for (Link link : tp.getLinks())
			link.setWidth(1);
	}
	
	@Override
	public void onSelection(Node node) {
		reset();
		buildFrom(node);
	}

	public static void main(String args[]){
		Topology tp = new Topology(500,400);
		new JViewer(tp);
		new DFS(tp);
	}
}
