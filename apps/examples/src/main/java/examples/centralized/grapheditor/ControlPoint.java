package examples.centralized.grapheditor;
import io.jbotsim.Point;

import io.jbotsim.Link;
import io.jbotsim.Node;


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
