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
package io.jbotsim.ui.painting;

import io.jbotsim.core.Link;

/**
 * <p>The {@link LinkPainter} is an element which is used to be called while trying to draw a {@link Link}.</p>
 */
public interface LinkPainter {
    /**
     * Paints the Links.
     *
     * @param uiComponent the graphical object to be drawn on.
     * @param link the {@link Link} to be drawn.
     */
    void paintLink(UIComponent uiComponent, Link link);
}
