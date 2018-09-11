package examples.centralized;

import jbotsim.Color;
import java.util.Random;
import java.util.Vector;


import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.MovementListener;
import jbotsim.event.TopologyListener;
import jbotsimx.ui.JViewer;



public class MultiAggregation implements TopologyListener, ConnectivityListener, MovementListener{

	class Sink extends Node{
        public Sink(){
            setColor(Color.red);
        }
    }
	protected Topology t;
	public MultiAggregation(Topology topo){
		this.t=topo;

		topo.addConnectivityListener(this);
		topo.addMovementListener(this);
		topo.setDefaultNodeModel(Node.class);
		topo.setNodeModel("sink", Sink.class);
		topo.addNode(50, 50, new Sink());
		topo.addNode(550, 50, new Sink());
		topo.addNode(550, 350, new Sink());
		topo.addNode(50, 350, new Sink());
		Random rand=new Random();
		for (int i=0; i<40; i++){
			topo.addNode(50+rand.nextDouble()*500, 50+rand.nextDouble()*300);
		}
	}
	public void onNodeAdded(Node n) {
		updateTrees();
	}
	public void onNodeRemoved(Node n) {
		updateTrees();
	}
	public void onLinkAdded(Link l) {
		l.setWidth(1);
	}
	public void onLinkRemoved(Link l) {
	}
	public void onMove(Node n) {
		updateTrees();
	}

	public void updateTrees(){
		Vector<Node> sinks=new Vector<Node>();
		for (Node n : t.getNodes()){
			if (n.getColor() == Color.red)
				sinks.add(n);
			else
				n.setProperty("father", null);
		}
		for (Link l : t.getLinks()){
			l.setWidth(1);
		}
		for (Node n : sinks)
			updateTree(n);
	}
	public void updateTree(Node node){
		Vector<Node> potentialSons=new Vector<Node>();
		for (Node neigh : node.getNeighbors()){
			if ((node.getCommonLinkWith(neigh).getWidth())==1)
				potentialSons.add(neigh);
		}
		double dist=getDistanceFromRoot(node);
		for (Node pson : potentialSons){
			if (dist+node.distance(pson) < getDistanceFromRoot(pson)){
				setFather(pson, node);
				updateTree(pson);
			}
		}
	}
	public double getDistanceFromRoot(Node n){
		if (n.getColor() == Color.red)
			return 0;
		Node father=(Node)n.getProperty("father");
		if (father==null)
			return Integer.MAX_VALUE;
		else
			return n.distance(father)+getDistanceFromRoot(father);
	}
	public void setFather(Node child, Node father){
		Node oldFather=(Node)child.getProperty("father");
		if (oldFather!=null)
			child.getCommonLinkWith(oldFather).setWidth(1);
		child.setProperty("father", father);
		child.getCommonLinkWith(father).setWidth(3);		
	}
	public static void main(String args[]){
		Topology topo=new Topology();
		new JViewer(topo);
		new MultiAggregation(topo);
	}
}
