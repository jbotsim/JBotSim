package examples.misc.mobilitymodels;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsim.event.TopologyListener;
import jbotsimx.ui.JViewer;

import java.util.Random;


public class SimpleHighway implements ClockListener, TopologyListener{
	Topology tp;
    Random rand=new Random();
	boolean voie=true;
    
	public SimpleHighway(Topology tp){
		this.tp=tp;
		tp.addTopologyListener(this);
		tp.addClockListener(this);
	}

	public void onNodeAdded(Node n) {
		n.setProperty("speed", new Double(rand.nextDouble()*50+30));
		n.setLocation(n.getX(),voie?200:186);
		n.setDirection(0);
        voie=!voie;
	}
	public void onClock() {
		for (Node n : tp.getNodes()){
			n.move((Double)n.getProperty("speed")/16.0);
			if (n.getX()>800)
				n.setLocation(0, n.getY());
		}
	}
	public void onNodeRemoved(Node n) {}
	
	public static void main(String args[]){
		Topology tp=new Topology();
		new SimpleHighway(tp);
		new JViewer(tp);
	}
}