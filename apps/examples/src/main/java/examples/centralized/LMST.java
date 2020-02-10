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
import io.jbotsim.core.Properties;
import io.jbotsim.core.event.ConnectivityListener;
import io.jbotsim.core.event.MovementListener;
import io.jbotsim.core.event.PropertyListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.contrib.algos.Algorithms;

import java.util.HashMap;
import java.util.List;


public class LMST implements ConnectivityListener, MovementListener, PropertyListener{
	Topology tp;
	public LMST(Topology tp){
		this.tp =tp;
		tp.addMovementListener(this);
        tp.addConnectivityListener(this);
	}
	@Override
	public void onMovement(Node n) {
		updateLMST();
	}
    @Override
    public void onLinkAdded(Link link) {
        updateLMST();
    }

    @Override
    public void onLinkRemoved(Link link) {
        updateLMST();
    }
	public void onPropertyChanged(Properties o, String key) {
		updateLMST();	
	}

	public void updateLMST(){
        HashMap<Node,List<Link>> allLocalMSTs=new HashMap<Node,List<Link>>();
		for (Node node : tp.getNodes()){
			List<Node> N=node.getNeighbors();
			N.add(node);
			allLocalMSTs.put(node, Algorithms.getMST(N));
		}
		for (Link l : tp.getLinks()){
			List<Link> mst1=allLocalMSTs.get(l.source);
			List<Link> mst2=allLocalMSTs.get(l.destination);
			if (mst1.contains(l) && mst2.contains(l)) {
                l.setWidth(3);
            }else
				l.setWidth(1);
		}		
	}
	
	public static void main(String args[]){
		Topology topo=new Topology();
		new JViewer(topo);
		new LMST(topo);
	}

}
