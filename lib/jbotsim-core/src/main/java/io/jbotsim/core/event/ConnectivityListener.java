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

import io.jbotsim.core.Link;

public interface ConnectivityListener {
    /**
     * Notifies the underlying listener that a link has been added.
     * @param link The added link.
     */
    void onLinkAdded(Link link);
    /**
     * Notifies the underlying listener that a link has been removed.
     * @param link The removed link.
     */
    void onLinkRemoved(Link link);
}
