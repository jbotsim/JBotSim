/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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
package io.jbotsim.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.jbotsim.core.event.PropertyListener;

public abstract class Properties {
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
     * @return the {@link Object} corresponding to the provided key
     */
    public Object getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Stores the specified property (<code>value</code>) under the specified name
     * (<code>key</code>).
     *
     * @param key   The property name.
     * @param value The property value.
     */
    public void setProperty(String key, Object value) {
        properties.put(key, value);
        for (PropertyListener pl : new ArrayList<>(propertyListeners))
            pl.onPropertyChanged(this, key);
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
     * @return <code>true</code> is the provided key corresponds to a known propery, <code>false</code> otherwise.
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

}
