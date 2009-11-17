/*******************************************************************************
 * This file is part of JBotSim.
 * 
 *     JBotSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JBotSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JBotSim.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     contributors:
 *     Arnaud Casteigts
 *******************************************************************************/
package jbotsim.core;

import jbotsim.core.event.*;

public class TopologyMaintainer implements TopologyListener, NodeListener{
    protected Topology topo;
    
    public TopologyMaintainer(Topology topo){
        this.topo=topo;
        topo.addTopologyListener(this);
    }

    protected void updateArc(Node n1, Node n2){
    	Link e=n1.getOutgoingLinkTo(n2);
    	if (e==null){
    		if(n1.wireless && n2.wireless && n1.coords.distance(n2.coords)<n1.communicationRange)
    			topo.addLink(new Link(n1,n2,Link.Type.DIRECTED,Link.Mode.WIRELESS));
    	}else{
    		if (e.wireless && (!n1.wireless || !n2.wireless || n1.distance(n2)>n1.communicationRange))
    			topo.removeLink(e);
    	}
    }

    protected void updateArcsFor(Node n){
        for (Node n2 : topo.getNodes()){
        	if (n2!=n){
        		updateArc(n, n2);
        		updateArc(n2, n);
        	}
        }
    }    
	public void manualUpdate(){
		for (Node n1 : topo.getNodes())
			for (Node n2 : topo.getNodes())
				if (n1!=n2)
					updateArc(n1, n2);
	}

	public void setMode(boolean automatic){
		if (automatic){
			topo.addTopologyListener(this);
			for (Node n:topo.getNodes())
				n.addNodeListener(this);
			manualUpdate();
		}else{
			topo.removeTopologyListener(this);
			for (Node n:topo.getNodes())
				n.removeNodeListener(this);
		}
	}
	public void nodeAdded(Node n){
    	n.addNodeListener(this);
    	updateArcsFor(n);
    }
    public void nodeRemoved(Node n){
        n.removeNodeListener(this);
    }
    public void nodeMoved(Node n){
    	updateArcsFor(n);
    }
	public void nodeChanged(Node n, String property) {
        if(property.equals("communicationRange") || property.equals("wireless"))
        		updateArcsFor(n);
	}
	public void linkAdded(Link e) {}
	public void linkRemoved(Link e) {}
}