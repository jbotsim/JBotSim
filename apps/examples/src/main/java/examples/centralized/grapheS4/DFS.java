package examples.centralized.grapheS4;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.SelectionListener;
import jbotsimx.ui.JViewer;

import jbotsim.Color;

public class DFS implements SelectionListener{
	Topology tp;
	
	public DFS(Topology tp){
		this.tp=tp;
		tp.addSelectionListener(this);
	}

	private void buildFrom(Node node){
		node.setColor(Color.black);
		for (Node ng : node.getNeighbors()){
			if (ng.getColor() != Color.black){
				ng.setColor(Color.black);
				node.getCommonLinkWith(ng).setWidth(3);
			}
		}
	}
	
	private void reset(){
		for (Node node : tp.getNodes())
			node.setColor(null);	
		for (Link link : tp.getLinks())
			link.setWidth(1);
	}
	
	@Override
	public void onSelection(Node node) {
		reset();
		buildFrom(node);
	}

	public static void main(String args[]){
		Topology tp = new Topology(500,400);
		new JViewer(tp);
		new DFS(tp);
	}
}
