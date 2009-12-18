/*******************************************************************************
 * This file is part of JBotSim.
 * 
 *     JBotSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JBotSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JBotSim.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     contributors:
 *     Arnaud Casteigts
 *******************************************************************************/
package jbotsim.event;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;

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
    /**
     * Notifies the underlying listener that a link has been added to the 
     * topology.
     * @param l The added link.
     */
    public void linkAdded(Link l);
    /**
     * Notifies the underlying listener that a node has been removed to the 
     * topology. 
     * @param l The removed link.
     */
    public void linkRemoved(Link l);
    /**
     * Notifies the underlying listener that a property of the topology has 
     * been changed. 
     * @param t The topology.
     * @param key The name of the changed property.
     */
    public void propertyChanged(Topology t, String key);
}
