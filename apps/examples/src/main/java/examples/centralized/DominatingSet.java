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

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import io.jbotsim.core.Color;

public class DominatingSet extends Node{

	public DominatingSet(){
		this.setColor(Color.black);
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	public void onLinkAdded(Link l) {
		update();
	}

	public void onLinkRemoved(Link l) {
		update();
	}
	
	public void update(){
		if (getColor() == Color.white){
			if (!isDominated(this))
				setColor(Color.black);
		}else{ 
			if (!isEssential(this))
				setColor(Color.white);
		}		
	}
	//////////////////////////////////////////////////////////////////////////
	
	public static boolean isDominated(Node v){
		return (getNumberOfConnectedDominators(v)!=0);
	}
	public static boolean isEssential(Node v){
		assert(v.getColor() == Color.black);
		if (getNumberOfConnectedDominators(v) == 1)
			return true;
		for (Node ng : v.getNeighbors())
			if (getNumberOfConnectedDominators(ng) == 1)
				return true;
		return false;
	}
	public static int getNumberOfConnectedDominators(Node v){
		int nbDom=0;
		if (v.getColor() == Color.black)
			nbDom++;
		for (Node ng : v.getNeighbors())
			if (ng.getColor() == Color.black)
				nbDom++;
		return nbDom;		
	}


	public static void main(String args[]){
		Topology tp = new Topology();
		tp.setDefaultNodeModel(DominatingSet.class);
		new JViewer(tp);
	}
}
