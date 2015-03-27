/*
 * Tikz export for objects of type Topology in JBotSim.
 * 
 *    This program is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts		<casteig@site.uottawa.ca>
 */
package jbotsimx;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;

public class Tikz {
	public static String exportTopology(Topology tp){
		return exportTopology(tp,50);
	}
	public static String exportTopology(Topology tp, int scale){
		String delim="\n";
		String s="\\begin{tikzpicture}[xscale=1,yscale=1]"+delim;
		Integer sr=(int)tp.getSensingRange();
		if (sr!=0){
			s=s+"  \\tikzstyle{every node}=[draw,circle,inner sep="+sr/5.0+", fill opacity=0.5,gray,fill=gray!40]"+delim;
			for (Node n : tp.getNodes()){
				double x=Math.round(n.getX()*100/scale)/100.0;
				double y=Math.round((600-n.getY())*100/scale)/100.0;
				s=s+"  \\path ("+x+","+y+") node ("+n+") {};"+delim;
			}
		}
		s=s+"  \\tikzstyle{every node}=[draw,circle,fill=gray,inner sep=1.5]"+delim;
		for (Node n : tp.getNodes()){
			String id = "v"+n.toString();
			double x=Math.round(n.getX()*100/scale)/100.0;
			double y=Math.round((600-n.getY())*100/scale)/100.0;
			s=s+"  \\path ("+x+","+y+") node ("+id+") {};"+delim;
		}
		s+="  \\tikzstyle{every path}=[];"+delim;
		for (Link l : tp.getLinks()) {
			String width="";
			if (l.getWidth()>1)
				width="[ultra thick]";
			String id1 = "v"+l.source.toString();
			String id2 = "v"+l.destination.toString();
			s += "  \\draw"+width+" (" + id1 + ")--(" + id2 + ");" + delim;
		}
		s+="\\end{tikzpicture}"+delim;
		return s;		
	}
}
