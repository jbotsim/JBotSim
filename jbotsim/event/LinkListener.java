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

import jbotsim.Link;

public interface LinkListener{
	/**
	 * Notifies the underlying listener that a property of the link has been 
	 * changed.
	 * @param l The link.
	 * @param key The name of the changed property.
	 */
    public void propertyChanged(Link l, String key);
}
