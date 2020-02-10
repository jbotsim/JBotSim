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

import io.jbotsim.core.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.MovementListener;
import io.jbotsim.ui.JViewer;

/* DFS Tree (Depth-First Search) = Arbre en profondeur */

public class BFSSolution implements MovementListener{
	Topology tp;
	Queue<Node> liste = new LinkedList<Node>();
	
	public BFSSolution(Topology tp){
		this.tp=tp;
		tp.addMovementListener(this);
	}

	private void reset(){
		for (Node node : tp.getNodes())
			node.setColor(null);
		for (Link link : tp.getLinks())
			link.setWidth(1);
	}
	
	private void buildFrom(Node node){
		node.setColor(Color.black);
		ArrayList<Node> children = new ArrayList<Node>();
		for (Node ng : node.getNeighbors()){
			if (ng.getColor() != Color.black){
				ng.setColor(Color.black);
				node.getCommonLinkWith(ng).setWidth(3);
				children.add(ng);
			}
		}
		for (Node ng : children)
			buildFrom(ng);
	}
	
	@Override
	public void onMovement(Node node) {
		reset();
		buildFrom(node);
	}

	public static void main(String args[]){
		Topology tp = new Topology();
		new JViewer(tp);
		new BFSSolution(tp);
	}
}
