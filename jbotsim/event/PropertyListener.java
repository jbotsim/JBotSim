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

public interface PropertyListener {
    /**
     * Notifies that a property of this object has changed.
     * @param o The object.
     * @param key The name of the changed property.
     */
    public void propertyChanged(Object o, String key);
}
