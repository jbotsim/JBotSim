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
package jbotsim.engine;

import java.util.Vector;
import jbotsim.core.Link;
import jbotsim.core.Message;
import jbotsim.core.Node;
import jbotsim.core.Topology;
import jbotsim.core.event.*;

public class MessageEngine extends AbstractEngine implements TopologyListener{
	
	public MessageEngine(Topology topo, int clockRate) {
		super(topo, clockRate);
		topo.addTopologyListener(this);
	}
	
	public void onClock() {
		if (running)
			deliverMessages();
	}
	
	protected void deliverMessages(){
		for (Node n : topo.getNodes())
			for (Message m : collectMessages(n))
				deliverMessage(m);
	}
	
	@SuppressWarnings("unchecked")
	protected Vector<Message> collectMessages(Node n){
		Vector<Message> sendQueue=(Vector<Message>)n.getProperty("sendQueue");
		Vector<Message> result=new Vector<Message>(sendQueue);
		sendQueue.clear();
		return result;
	}
	protected void deliverMessage(Message m){
		if (m.to!=null)
			deliverMessageTo(m.to, m);
		else
			for(Link l : m.from.getOutgoingLinks())
				deliverMessageTo(l.getDestinationNode(), m);
	}
	@SuppressWarnings("unchecked")
	protected void deliverMessageTo(Node n, Message m){
		((Vector<Message>)n.getProperty("mailBox")).add(m);
	}
	
	// From TopologyListener ///////	
	public void nodeAdded(Node n) {
		n.setProperty("sendQueue",new Vector<Message>());
		n.setProperty("mailBox",new Vector<Message>());
	}
	public void nodeRemoved(Node n) {}
	public void linkAdded(Link e) {}
	public void linkRemoved(Link e) {}
}
