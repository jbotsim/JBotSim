package examples.misc.mobilitymodels;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;


public class CircleMovement extends Node implements ClockListener{   
	public void onClock() {
		setDirection((getDirection()-0.1)%(2.0*Math.PI));
		move(3);
	}
	public static void main(String args[]){
		Topology tp=new Topology();
		tp.setDefaultNodeModel(CircleMovement.class);
		new jbotsimx.ui.JViewer(tp);
	}
}