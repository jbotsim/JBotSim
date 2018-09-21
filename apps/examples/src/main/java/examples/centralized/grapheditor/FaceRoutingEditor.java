package examples.centralized.grapheditor;

import javax.swing.*;



import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import jbotsim.Point;
import java.util.*;

import jbotsim.*;

public class FaceRoutingEditor extends GraphEditor {
	
	public FaceRoutingEditor(Topology t){
		super(t);
		JButton cmdPlanar=new JButton("planarize !");
		cmdPlanar.setActionCommand("planar");
		cmdPlanar.addActionListener(this);
		mainPanel.add(cmdPlanar, BorderLayout.SOUTH);
	}
	public static void main(String[] args) {
		Topology t=new Topology();
		//Node n=new Node();
		//n.setWireless(false);
		//n.setProperty("size", 4);
		//t.addNodeModel("center", n);
		new FaceRoutingEditor(t);
	}
	public void actionPerformed(ActionEvent action) {
		super.actionPerformed(action);
		if (action.getActionCommand().endsWith("planar"))
			planarize();
	}
	public void doExport(){
		for (Node tmp : topo.getNodes()){
			tmp.setProperty("x", tmp.getX());
			tmp.setProperty("y", tmp.getY());
		}
		
		Vector<ControlPoint> allControlPoints=new Vector<ControlPoint>();
		String delim="\n";
		double scale=50;
		
		//genere le texte
		String s="\\begin{tikzpicture}[xscale=1,yscale=1]"+delim;
		s=s+"  \\tikzstyle{every node}=[draw,circle,inner sep=2]"+delim;
		Vector<String> labels=new Vector<String>();
		for (Node n : topo.getNodes()){
			s=s+"  \\path ("+(n.getX())/scale+","+(viewer.getHeight()-n.getY())/scale+") node ("+n+") {};"+delim;
			if (!isDefault(n.toString()))
				labels.add(n.toString());
			int idc=97;
			Vector<ControlPoint> controlPoints=getControlPoints(topo,n);
			allControlPoints.addAll(controlPoints);
			for (Enumeration<ControlPoint> e=controlPoints.elements();e.hasMoreElements();){
				ControlPoint cp=e.nextElement();
				cp.id=n.toString()+new String(Character.toChars(idc++));
				s=s+"  \\path ("+((double)(int)cp.point.x)/scale+","+
					((double)(int)(viewer.getHeight()-cp.point.y))/scale+") coordinate ("+cp.id+") {};"+delim;
			}
		}
		if (labels.size()>0){
			s=s+"  \\tikzstyle{every node}=[];"+delim;
			for (Enumeration<String> en=labels.elements();en.hasMoreElements();){
				String label=en.nextElement();
				s=s+"  \\path ("+label+")+(.3,.3) node (label"+label+") {\\small "+label+"};"+delim;
			}
		}
		s=s+"  \\tikzstyle{every path}=[];"+delim;
		//s=s+"  \\draw (6,8) rectangle (12,13);"+delim;
		for (Link e : topo.getLinks()){
			String from=e.source.toString();
			String to=e.destination.toString();
			s=s+"  \\draw ("+from+")--("+to+");"+delim;
		}
		s=s+"  \\tikzstyle{every path}=[red, rounded corners=.3cm, dashed];"+delim;
		while (!allControlPoints.isEmpty()){
			ControlPoint cp=allControlPoints.elementAt(0);
			System.out.println("starting at "+cp.id);
			s=s+"  \\draw ("+cp.id+")";
			Vector<ControlPoint> passedPoints=new Vector<ControlPoint>();
			passedPoints.add(cp);
			ControlPoint next=cp;
			while((next=getNextControlPoint(allControlPoints, cp))!=null){
				passedPoints.add(next);
				s=s+"--("+next.id+")";
				cp=next;
			}
			s=s+"--cycle;"+delim;
			allControlPoints.removeAll(passedPoints);
		}
		s=s+"\\end{tikzpicture}"+delim;
		textArea.setText(s);	
	}
	public ControlPoint getNextControlPoint(Vector<ControlPoint> all, ControlPoint cpfrom){
		System.out.println("looking for the next of "+cpfrom.id);
		if(cpfrom.visited){
			System.out.println("null");
			return null;
		}else{
			cpfrom.visited=true;
		}
		for (Enumeration<ControlPoint> en=all.elements(); en.hasMoreElements();){
			ControlPoint cp=en.nextElement();
			if (cp.inLink == cpfrom.outLink && cp.n!=cpfrom.n){
				System.out.println("find "+cp.id);
				if (cp.visited==false)
					return cp;
				else
					return null;
			}
		}
		// Si on est encore la, c'est qu'il s'agit d'une arete a contourner
		for (Enumeration<ControlPoint> en=all.elements(); en.hasMoreElements();){
			ControlPoint cp=en.nextElement();
			if (cpfrom!=cp && cpfrom.n==cp.n)
				return cp;
		}
		return null;
	}
	public Link getAssociatedLink(Point p){
		//Link l=table.get(p).elementAt(0);
		return null;
	}
	public Point getOtherControlPoint(Point p, Link l){
		return new Point();
	}
	public void planarize(){
		for (Link l : topo.getLinks()){
			if (l.source.getCommonLinkWith(l.destination)!=null){
				boolean toremove=false;
				for (Enumeration<Node> en=getCommonNeighbors(l.source,l.destination).elements();en.hasMoreElements();){
					if (isTheLonger(l,en.nextElement())){
						toremove=true;
						break;
					}
				}
				if (toremove){
					topo.removeLink(l.source.getCommonLinkWith(l.destination));
				}
			}
		}
	}
	protected boolean isTheLonger(Link l, Node commonNeighbor){
		Node from=l.source;
		Node to=l.destination;
		return (from.distance(to)>from.distance(commonNeighbor) &&
				from.distance(to)>to.distance(commonNeighbor));
	}
	protected Vector<Node> getCommonNeighbors(Node n1, Node n2){
		Vector<Node> result=new Vector<Node>();
		for (Link l1 : n1.getOutLinks()){
			Node neig1=l1.destination;
			for (Link l2 : n2.getOutLinks()){
				Node neig2=l2.destination;
				if (neig1==neig2)
					result.add(neig1);
			}
		}
		return result;
	}
	protected Vector<ControlPoint> getControlPoints(Topology g, Node v){
		Vector<ControlPoint> result=new Vector<ControlPoint>();
    	Point p=new Point((Double)v.getProperty("x"), (Double)v.getProperty("y"));
    	Hashtable<Double, Link> angles=new Hashtable<Double, Link>();
    	for (Link e : v.getLinks()){
    		Node ng=e.getOtherEndpoint(v);
    		Point pn=new Point((Double)ng.getProperty("x"), (Double)ng.getProperty("y"));
    		Double angle=Math.atan2(pn.y-p.y, pn.x-p.x);
    		if (angle<0) angle+=2*Math.PI;
    		angles.put(angle, e);
    	}
    	if (angles.size()==0){ 
    		return result;
    	}else if (angles.size()==1){
    		Double angle=angles.keys().nextElement();
    		ControlPoint cp1=getPoint(p,angle+3*Math.PI/4,3*Math.PI/2);
    		cp1.outLink=angles.get(angle);
    		cp1.n=v;
    		ControlPoint cp2=getPoint(p,angle-3*Math.PI/4,3*Math.PI/2);
    		cp2.inLink=angles.get(angle);
    		cp2.n=v;
    		result.add(cp1);
    		result.add(cp2);
    		return result;
    	}
    	
    	Vector<Double> orderedAngles=new Vector<Double>(angles.keySet());
    	Collections.sort(orderedAngles);
    	orderedAngles.add(orderedAngles.elementAt(0));
    	
    	Enumeration<Double> en=orderedAngles.elements();
    	Double angle1=en.nextElement();
    	while(en.hasMoreElements()){
    		Double angle2=en.nextElement();
    		Double diff=(angle2-angle1);
    		if (diff<0) diff+=Math.PI*2;
    		Double medium=angle1+(diff)/2;
    		ControlPoint cp=getPoint(p,medium,diff);
    		cp.inLink=angles.get(angle2);
    		cp.outLink=angles.get(angle1);
    		cp.n=v;
    		result.add(cp);
    		angle1=angle2;
    	}
    	return result;
	}
	protected ControlPoint getPoint(Point p, double angle, double anglediff){
		double distance;
		if (anglediff<Math.PI/2)
			distance=(anglediff+3)*3;
		else if (anglediff<(Math.PI+.5) && anglediff>(Math.PI-.5))
			distance=(anglediff+1)*3;
		else
			distance=(anglediff+2)*3;
		double x=(p.x+Math.cos(angle)*distance);
		double y=(p.y+Math.sin(angle)*distance);
		return new ControlPoint(x,y);
	}
}
