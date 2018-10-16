package io.jbotsim.geometry.toroidal;


import io.jbotsim.core.LinkResolver;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;


/**
 * A new type of link resolver based on the toroidal distance between nodes.
 * Here, coordinates are assumed to be 2D (only) and nodes to be wireless.
 */

public class ToroidalLinkResolver extends LinkResolver {
	public double toroidalDistance(Node n1, Node n2){
		int width = n1.getTopology().getWidth();
		int height = n1.getTopology().getHeight();
		Point p1 = n1.getLocation();
		Point p2 = n2.getLocation();
		double distX = Math.abs((p1.getX() - p2.getX()));
		distX = Math.min(distX, width - distX);
		double distY = Math.abs((p1.getY() - p2.getY()));
		distY = Math.min(distY, height - distY);
		return Math.sqrt(distX*distX + distY*distY);
	}
	@Override
	public boolean isHeardBy(Node n1, Node n2) {
		return (toroidalDistance(n1, n2) < n1.getCommunicationRange());
	}
}
