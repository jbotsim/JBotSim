package examples.misc.mobilitymodels;

import jbotsim.Point;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsimx.ui.JViewer;

public class GlobalRWP implements ClockListener{
	Topology tp;
	public GlobalRWP (Topology tp){
		this.tp = tp;
		tp.addClockListener(this);
	}
	public void onClock(){
		for (Node n : tp.getNodes()){
			Point target = (Point)n.getProperty("target");
			if (target == null || n.getLocation().distance(target) < 2){
				target = new Point(Math.random()*400, Math.random()*300);
				n.setProperty("target", target);
			}
			n.setDirection(target);
			n.move();
		}
	}
	public static void main(String[] args){
		Topology tp = new Topology(400,300);
		new GlobalRWP(tp);
		new JViewer(tp);
	}
}
