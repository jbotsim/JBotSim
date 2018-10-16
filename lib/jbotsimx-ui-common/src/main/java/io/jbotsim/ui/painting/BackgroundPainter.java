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
package io.jbotsim.ui.painting;

import io.jbotsim.core.Topology;

/**
 * Created by acasteig on 6/9/15.
 */
public interface BackgroundPainter {
    /**
     * Provides a way to draw things on the background.
     *
     * @param g2d The background graphics.
     */
    void paintBackground(UIComponent g2d, Topology tp);
}
