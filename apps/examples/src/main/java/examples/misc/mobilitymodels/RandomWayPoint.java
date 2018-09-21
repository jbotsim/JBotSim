package examples.misc.mobilitymodels;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsim.event.TopologyListener;

import jbotsim.Point;
import jbotsimx.ui.JViewer;

import java.util.Random;

public class RandomWayPoint implements TopologyListener, ClockListener{
	Topology tp;
	public boolean isRunning=true;
	Random r=new Random();
	Point dim;
	boolean selective; // moves only the nodes whose "type" property is "rwp"

	public RandomWayPoint(Topology tp){
		this(tp, false);
	}
	public RandomWayPoint(Topology tp, boolean selective){
		this.tp = tp;
		dim = new Point(tp.getWidth(),tp.getHeight());
		this.selective = selective;
		tp.addTopologyListener(this);
		tp.addClockListener(this);
	}
	public void onNodeAdded(Node n) {
		String type=(String)n.getProperty("type");
		if ((type!=null && type.equals("rwp")) || !selective)
			n.setProperty("target", new Point(r.nextInt((int) dim.x), r.nextInt((int) dim.y)));
	}
	public void onNodeRemoved(Node n) {
	}
	public void onClock() {
		if (isRunning){
		for (Node n : tp.getNodes()){
			String type=(String)n.getProperty("type");
			if ((type!=null && type.equals("rwp")) || !selective){
				Point target=(Point)n.getProperty("target");
		        n.setDirection(target);
		    	n.move(2);
		        if(n.distance(target)<2)
		        	n.setProperty("target", new Point(r.nextInt((int) dim.x), r.nextInt((int) dim.y)));
			}
		}
		}
	}
	public static void main(String args[]){
		Topology tp=new Topology();
		new RandomWayPoint(tp);
		new JViewer(tp);
	}
}