package examples.centralized.grapheS4;

import jbotsim.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.MovementListener;
import jbotsimx.ui.JViewer;

/* DFS Tree (Depth-First Search) = Arbre en profondeur */

public class BFSSolution implements MovementListener{
	Topology tp;
	Queue<Node> liste = new LinkedList<Node>();
	
	public BFSSolution(Topology tp){
		this.tp=tp;
		tp.addMovementListener(this);
	}

	private void reset(){
		for (Node node : tp.getNodes())
			node.setColor(null);
		for (Link link : tp.getLinks())
			link.setWidth(1);
	}
	
	private void buildFrom(Node node){
		node.setColor(Color.black);
		ArrayList<Node> children = new ArrayList<Node>();
		for (Node ng : node.getNeighbors()){
			if (ng.getColor() != Color.black){
				ng.setColor(Color.black);
				node.getCommonLinkWith(ng).setWidth(3);
				children.add(ng);
			}
		}
		for (Node ng : children)
			buildFrom(ng);
	}
	
	@Override
	public void onMove(Node node) {
		reset();
		buildFrom(node);
	}

	public static void main(String args[]){
		Topology tp = new Topology();
		new JViewer(tp);
		new BFSSolution(tp);
	}
}
