package jbotsimx;

import java.util.Enumeration;

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
		Integer sr=(int)Node.getModel("default").getSensingRange();
		if (sr!=0){
			s=s+"  \\tikzstyle{every node}=[draw,circle,inner sep="+sr/5.0+", fill opacity=0.5,gray,fill=gray!40]"+delim;
			for (Enumeration<Node> en=tp.getNodes().elements(); en.hasMoreElements();){
				Node n=en.nextElement();
				double x=Math.round(n.getX()*100/scale)/100.0;
				double y=Math.round((600-n.getY())*100/scale)/100.0;
				s=s+"  \\path ("+x+","+y+") node ("+n+") {};"+delim;
			}
		}
		s=s+"  \\tikzstyle{every node}=[draw,circle,fill=gray,inner sep=1.5]"+delim;
		for (Enumeration<Node> en=tp.getNodes().elements(); en.hasMoreElements();){
			Node n=en.nextElement();
			double x=Math.round(n.getX()*100/scale)/100.0;
			double y=Math.round((600-n.getY())*100/scale)/100.0;
			s=s+"  \\path ("+x+","+y+") node ("+n+") {};"+delim;
		}
		s+="  \\tikzstyle{every path}=[];"+delim;
		for (Link l : tp.getLinks())
			s+="  \\draw ("+l.source+")--("+l.destination+");"+delim;
		s+="\\end{tikzpicture}"+delim;
		return s;		
	}
}
