package jbotsimx.tvgraph;

import java.util.TreeSet;
import java.util.Vector;

import jbotsim.Node;

public class TVGraph{
	Vector<Node> nodes=new Vector<Node>();
	Vector<TVLink> tvlinks=new Vector<TVLink>();
	public void addNode(Node n){
		nodes.add(n);
	}
	public void addTVLink(TVLink l){
		tvlinks.add(l);
	}
	public Vector<Node> getNodes(){
		return new Vector<Node>(nodes);
	}
	public Vector<TVLink> getTVLinks(){
		return new Vector<TVLink>(tvlinks);
	}
	public int nbDates(){
		TreeSet<Integer> allDates=new TreeSet<Integer>();
		for (TVLink l : tvlinks){
			allDates.addAll(l.appearanceDates);
			allDates.addAll(l.disappearanceDates);
		}
		return allDates.size();
	}
	public String toString(){
		String s="";
		for (TVLink l : tvlinks)
			s += l.toString()+"\n";
		return s;
	}
}
