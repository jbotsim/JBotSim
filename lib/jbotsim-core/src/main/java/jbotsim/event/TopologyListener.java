/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim.event;

import jbotsim.Node;

public interface TopologyListener{
    /**
     * Notifies the underlying listener that a node has been added to the
     * topology.
     * @param node The added node.
     */
    void onNodeAdded(Node node);
    /**
     * Notifies the underlying listener that a node has been removed to the 
     * topology.
     * @param node The removed node.
     */
    void onNodeRemoved(Node node);
}
