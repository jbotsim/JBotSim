package examples.centralized.grapheditor;
import java.util.Comparator;

import jbotsim.Node;


public class NodeComparator implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		double diffX=n1.getX()-n2.getX();
		if (diffX!=0)
			return (diffX<0)?-1:1;
		double diffY=n1.getY()-n2.getY();
		if (diffY!=0)
			return (diffY<0)?-1:1;
		return 0;
	}
}
