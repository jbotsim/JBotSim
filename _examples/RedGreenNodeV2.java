package _examples;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.MovementListener;

public class RedGreenNodeV2 extends Node implements ConnectivityListener,MovementListener{
	public RedGreenNodeV2(){
		super.setProperty("color", "red");
		super.addConnectivityListener(this);
		super.addMovementListener(this);
	}
	public void linkAdded(Link l) {
		super.setProperty("color", "green");
	}
	public void linkRemoved(Link l) {
		if (super.getNeighbors().size()==0)
			super.setProperty("color", "red");
	}
	public void nodeMoved(Node n) {}
	public void propertyChanged(Node n, String key) {}
}
