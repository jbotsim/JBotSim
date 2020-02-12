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

import io.jbotsim.core.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ConnectivityListener;
import io.jbotsim.ui.JViewer;

public class Coloring implements ConnectivityListener{

	Topology tp;
	List<Color> usedColors;
	
	public Coloring(Topology tp){
		this.tp = tp;
		tp.addConnectivityListener(this);
	}
	public void onLinkAdded(Link link) {
		updateColoring();
	}

	public void onLinkRemoved(Link link) {
		updateColoring();
	}

	private Color nextColor(){
		if (usedColors.size()<8){
			Color color;
			do{
				color = getColor((new Random()).nextInt(8));
			}while (usedColors.contains(color));
			usedColors.add(color);
			return color;
		}else
			return null;
	}
	private void updateColoring(){
		usedColors = new ArrayList<Color>();
		for (Node node : tp.getNodes())
			node.setColor(null);
		for (Node node : tp.getNodes()){
			for (Color color : usedColors){
				if (!getNeighboringColors(node).contains(color)){
					node.setColor(color);
					break;
				}
			}
			if (node.getColor() == null){
				node.setColor(nextColor());
			}
		}
	}
	protected Color getColor(int value){
		switch(value){
			case 0: return Color.black;
			case 1: return Color.white;
			case 2: return Color.red;
			case 3: return Color.blue;
			case 4: return Color.green;
			case 5: return Color.pink;
			case 6: return Color.orange;
			case 7: return Color.yellow;
			default: return Color.gray;
		}
	}

	private Set<Color> getNeighboringColors(Node node){
		Set<Color> colors = new HashSet<Color>();
		for (Node ng : node.getNeighbors())
			colors.add(ng.getColor());
		return colors;
	}
	
	public static void main(String[] args){
		Topology tp = new Topology();
		new Coloring(tp);
		JViewer jv = new JViewer(tp);
	}
}
