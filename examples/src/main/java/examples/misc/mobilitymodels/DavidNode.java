package examples.misc.mobilitymodels;

import jbotsim.Point;
import java.util.Random;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class DavidNode extends Node{

	Point vec;

    @Override
    public void onStart() {
		vec = new Point(0.0,0.0);
    }

    public void onClock() {
		double randx = ((new Random()).nextInt(3)-1)/10.0;
		double randy = ((new Random()).nextInt(3)-1)/10.0;

		vec.setLocation(vec.getX()+randx, vec.getY()+randy);
        Point next = new Point(getX()+vec.getX(),getY()+vec.getY());
		setLocation(next);
		wrapLocation();
	}
	public static void main(String args[]){
		Topology tp = new Topology();
		tp.setDefaultNodeModel(DavidNode.class);
		new JViewer(tp);
	}
}
