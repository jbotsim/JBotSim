package _examples;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.TopologyListener;

public class RedGreenCentralized implements TopologyListener, ConnectivityListener{
	
	/* Methods from TopologyListener */
	public void nodeAdded(Node n) {
		n.setProperty("color", n.getNeighbors().size()==0?"red":"green");
	}
	public void nodeRemoved(Node n) {}
	
	/* Methods from ConnectivityListener */
	public void linkAdded(Link l) {
		for (Node n : l.endpoints())
			n.setProperty("color", "green");
	}
	public void linkRemoved(Link l) {
		for (Node n : l.endpoints())
			if (n.getNeighbors().size()==0)
				n.setProperty("color", "red");
	}
}
