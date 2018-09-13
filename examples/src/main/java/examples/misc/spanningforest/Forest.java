package examples.misc.spanningforest;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.TopologyListener;

import jbotsim.Color;
import java.util.Random;

public class Forest implements ClockListener, ConnectivityListener, TopologyListener{
	Topology tp;
	
	public Forest(Topology tp){
		this.tp = tp;
		tp.addTopologyListener(this);
		tp.addClockListener(this);
		tp.addConnectivityListener(this);
	}

	@Override
	public void onNodeAdded(Node node) {
		setDefaultState(node);
	}

	@Override
	public void onNodeRemoved(Node node) {
	}

	@Override
	public void onClock() {
		int nbLinks = tp.getLinks().size();		
		if (nbLinks > 0)
			applyRule(tp.getLinks().get((new Random()).nextInt(nbLinks)));
	}

	@Override
	public void onLinkAdded(Link link) {
	}

	@Override
	public void onLinkRemoved(Link l) {
		// Si arête de l'arbre dans un sens
		if ((Node)l.endpoints().get(0).getProperty("parent")==l.endpoints().get(1))
			setDefaultState(l.endpoints().get(0));
		// dans l'autre sens
		else if ((Node)l.endpoints().get(1).getProperty("parent")==l.endpoints().get(0))
			setDefaultState(l.endpoints().get(1));
		// Sinon je fais rien
	}	
	
	public void setDefaultState(Node n) {
		n.setColor(Color.red);
		n.setProperty("parent", n); // root is its own parent
	}

	public void applyRule(Link l) {
		if(l.endpoints().get(0).getColor() == Color.red
				&& l.endpoints().get(1).getColor() == Color.red){
			if (Math.random()<.5)
				setRelation(l.endpoints().get(0), l.endpoints().get(1));
			else
				setRelation(l.endpoints().get(1), l.endpoints().get(0));
			l.setWidth(3);
		}
		else if(l.endpoints().get(0).getColor() == Color.red
				&& l.endpoints().get(1).getColor() == Color.blue
				&& l.getWidth() == 3){
			setRelation(l.endpoints().get(0), l.endpoints().get(1));
		}
		else if(l.endpoints().get(0).getColor() == Color.blue
				&& l.endpoints().get(1).getColor() == Color.red
				&& l.getWidth() == 3){
			setRelation(l.endpoints().get(1), l.endpoints().get(0));
		}
	}
	
	protected void setRelation(Node child, Node parent){
		child.setColor(Color.blue);
		child.setProperty("parent", parent);
		parent.setColor(Color.red);
		parent.setProperty("parent", parent);
	}
}
