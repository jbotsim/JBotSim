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
import jbotsim.engine.event.ClockListener;

public class TopologyMaintainer implements TopologyListener, NodeListener, ClockListener{
    protected Topology topo;
    protected boolean stroboscopic=false;
    protected boolean touched=false;
    
    public TopologyMaintainer(Topology topo){
    	this (topo, 0);
    }
    
    public TopologyMaintainer(Topology topo, int refreshRate){
        this.topo=topo;
        if (refreshRate>0){
        	topo.getClock().addClockListener(this, refreshRate);
        	this.stroboscopic=true;
        }
        topo.addTopologyListener(this);
    }

    public void nodeAdded(Node n){
    	n.addNodeListener(this);
    	if (stroboscopic)
    		touched=true;
    	else
    		updateArcsFor(topo, n);
    }
    public void nodeRemoved(Node n){
        n.removeNodeListener(this);
    }
    public void nodeMoved(Node n){
    	if (stroboscopic)
    		touched=true;
    	else
    		updateArcsFor(topo, n);
    }
	public void nodeChanged(Node n, String property) {
        if(property.equals("communicationRange") || property.equals("wireless")){
        	if (stroboscopic)
        		touched=true;
        	else
        		updateArcsFor(topo, n);
        }
	}
	public void onClock() {
		if (touched){
			updateTopology(topo);
			touched=false;
		}
	}
	public void linkAdded(Link e) {}
	public void linkRemoved(Link e) {}
	
    protected static void updateArc(Topology t, Node n1, Node n2){
    	Link e=n1.getOutgoingLinkTo(n2);
    	if (e==null){
    		if(n1.wireless && n2.wireless && n1.coords.distance(n2.coords)<n1.communicationRange)
    			t.addLink(new Link(n1,n2,Link.Type.DIRECTED,Link.Mode.WIRELESS));
    	}else{
    		if (e.wireless && (!n1.wireless || !n2.wireless || n1.distance(n2)>n1.communicationRange))
    			t.removeLink(e);
    	}
    }

    protected static void updateArcsFor(Topology t, Node n){
        for (Node n2 : t.getNodes()){
        	if (n2!=n){
        		updateArc(t, n, n2);
        		updateArc(t, n2, n);
        	}
        }
    }    
	public static void updateTopology(Topology t){
		for (Node n1 : t.getNodes())
			for (Node n2 : t.getNodes())
				if (n1!=n2)
					updateArc(t, n1, n2);
	}
}