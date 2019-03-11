package examples.misc.mobilitymodels;

import io.jbotsim.core.Point;
import java.util.Random;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

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
		tp.start();
	}
}
