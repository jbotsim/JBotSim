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
package jbotsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jbotsim.event.PropertyListener;

public abstract class _Properties {
    protected HashMap<String, Object> properties = new HashMap<>();
    List<PropertyListener> propertyListeners = new ArrayList<>();

    /**
     * Registers the specified property listener to this node. The listener
     * will be notified every time a property of this node changes.
     *
     * @param listener The movement listener.
     */
    public void addPropertyListener(PropertyListener listener) {
        propertyListeners.add(listener);
    }

    /**
     * Unregisters the specified property listener for this node.
     *
     * @param listener The property listener.
     */
    public void removePropertyListener(PropertyListener listener) {
        propertyListeners.remove(listener);
    }

    /**
     * Returns the property stored under the specified key.
     *
     * @param key The property key.
     */
    public Object getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Stores the specified property (<tt>value</tt>) under the specified name
     * (<tt>key</tt>).
     *
     * @param key   The property name.
     * @param value The property value.
     */
    public void setProperty(String key, Object value) {
        properties.put(key, value);
        for (PropertyListener pl : new ArrayList<>(propertyListeners))
            pl.propertyChanged(this, key);
    }

    /**
     * Removes the specified property.
     *
     * @param key The property key.
     */
    public void removeProperty(String key) {
        properties.remove(key);
    }

    /**
     * Returns the property stored under the specified key.
     *
     * @param key The property key.
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

}
