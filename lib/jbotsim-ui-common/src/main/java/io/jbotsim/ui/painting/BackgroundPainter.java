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
 * <p>The {@link BackgroundPainter} is an element which is used to be called while trying to draw a background.</p>
 */
public interface BackgroundPainter {
    /**
     * Provides a way to draw things on the background.
     *
     * @param uiComponent the graphical object to be drawn on.
     * @param tp the {@link Topology} to pick information from.
     */
    void paintBackground(UIComponent uiComponent, Topology tp);
}
