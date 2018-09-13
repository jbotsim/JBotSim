package examples.centralized.grapheditor;
import jbotsim.Point;

import jbotsim.Link;
import jbotsim.Node;


public class ControlPoint {
	public Link inLink;
	public Link outLink;
	public Node n;
	public Point point;
	public boolean visited=false;
	public String id;
	public ControlPoint(double x, double y){
		point = new Point(x,y);
	}
}
