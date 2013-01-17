package _examples;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.event.ConnectivityListener;

public class RedGreenNodeV2 extends Node implements ConnectivityListener{
	public RedGreenNodeV2(){
		super.setProperty("color", "red");
		super.addConnectivityListener(this);
	}
	public void linkAdded(Link l) {
		super.setProperty("color", "green");
	}
	public void linkRemoved(Link l) {
		if (super.getNeighbors().size()==0)
			super.setProperty("color", "red");
	}
}
