/*
 * This file is part of JBotSim.
 * 
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts		<casteig@site.uottawa.ca>
 */
package jbotsim.event;

import jbotsim.Node;

public interface TopologyListener{
	/**
	 * Notifies the underlying listener that a node has been added to the 
     * topology.
	 * @param n The added node.
	 */
    public void nodeAdded(Node n);
    /**
     * Notifies the underlying listener that a node has been removed to the 
     * topology.
     * @param n The removed node.
     */
    public void nodeRemoved(Node n);
}
