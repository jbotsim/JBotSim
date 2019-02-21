/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
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
