package examples.centralized.grapheditor;
import io.jbotsim.core.Point;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;


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
