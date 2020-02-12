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
package io.jbotsim.contrib.obstacle.ui;

import io.jbotsim.contrib.obstacle.core.Obstacle;
import io.jbotsim.ui.painting.UIComponent;

/**
 * Interface for drawable obstacles (3D obstacles and 2D obstacles)
 *
 */
public interface ObstacleDrawable extends Obstacle {

    /**
     * This function is automatically call by ObstaclePainter to draw the obstacle
     * @param uiComponent The UIComponent on which the obstacle will be drawn
     */
    void paint(UIComponent uiComponent);
    
}
