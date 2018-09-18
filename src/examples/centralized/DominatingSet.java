package examples.centralized;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

import jbotsim.Color;

public class DominatingSet extends Node{

	public DominatingSet(){
		this.setColor(Color.black);
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	public void onLinkAdded(Link l) {
		update();
	}

	public void onLinkRemoved(Link l) {
		update();
	}
	
	public void update(){
		if (getColor() == Color.white){
			if (!isDominated(this))
				setColor(Color.black);
		}else{ 
			if (!isEssential(this))
				setColor(Color.white);
		}		
	}
	//////////////////////////////////////////////////////////////////////////
	
	public static boolean isDominated(Node v){
		return (getNumberOfConnectedDominators(v)!=0);
	}
	public static boolean isEssential(Node v){
		assert(v.getColor() == Color.black);
		if (getNumberOfConnectedDominators(v) == 1)
			return true;
		for (Node ng : v.getNeighbors())
			if (getNumberOfConnectedDominators(ng) == 1)
				return true;
		return false;
	}
	public static int getNumberOfConnectedDominators(Node v){
		int nbDom=0;
		if (v.getColor() == Color.black)
			nbDom++;
		for (Node ng : v.getNeighbors())
			if (ng.getColor() == Color.black)
				nbDom++;
		return nbDom;		
	}


	public static void main(String args[]){
		Topology tp = new Topology();
		tp.setDefaultNodeModel(DominatingSet.class);
		new JViewer(tp);
	}
}
