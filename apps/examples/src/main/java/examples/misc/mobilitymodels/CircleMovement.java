package examples.misc.mobilitymodels;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;


public class CircleMovement extends Node implements ClockListener{   
	public void onClock() {
		setDirection((getDirection()-0.1)%(2.0*Math.PI));
		move(3);
	}
	public static void main(String args[]){
		Topology tp=new Topology();
		tp.setDefaultNodeModel(CircleMovement.class);
		new JViewer(tp);
	}
}