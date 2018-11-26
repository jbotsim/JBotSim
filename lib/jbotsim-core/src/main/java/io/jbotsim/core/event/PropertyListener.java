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
package io.jbotsim.core.event;

import io.jbotsim.core.Properties;

public interface PropertyListener {
    /**
     * Notifies that a property of this object has changed.
     * @param o The object.
     * @param key The name of the changed property.
     */
    void onPropertyChanged(Properties o, String key);
}
