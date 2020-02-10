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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.ui.JViewer;

public class Dijkstra implements SelectionListener {
	Topology tp;
	Set<Node> finished = new HashSet<Node>();
	HashMap<Node,Double> weights = new HashMap<Node,Double>();
	
	public Dijkstra(Topology tp){
		this.tp=tp;
		tp.addSelectionListener(this);
	}

	public void computeDijkstraFrom(Node src){
		
		// TODO quitter si non connexe
			
		
		// Remet l'épaisseur des arêtes à 1
		for (Link l : tp.getLinks())
			l.setWidth(1);
		
		// Initialise le poids des sommets à +infini
		for (Node node : tp.getNodes())
			weights.put(node, Double.MAX_VALUE);
		
		// Poids 0 pour la source
		weights.put(src,0.0);
		
		while (finished.size()!=tp.getNodes().size()){ // Tant qu'il reste des noeuds à examiner
			Node vtmp = getMinNode(); // Récupère le sommet non-fini de poids min
			// TODO implémenter le coeur de l'algorithme
		}
		
		// TODO colorie les arêtes qui ont été choisies
	}
	
	private Node getMinNode(){
		// TODO trouver le sommet non-fini de poids minimum
		return null;
	}
	
	@Override
	public void onSelection(Node node) {
		computeDijkstraFrom(node);
	}

	public static void main(String args[]){
		Topology tp = new Topology();
		new Dijkstra(tp);
		new JViewer(tp);
	}
}