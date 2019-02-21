/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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
package io.jbotsim.contrib.obstacle.shapes3d;

import io.jbotsim.core.Point;
import io.jbotsim.contrib.obstacle.ui.ObstacleDrawable;
import io.jbotsim.ui.painting.UIComponent;

public class RectangularFacetObstacleDrawable extends RectangularFacetObstacle implements ObstacleDrawable {

    public RectangularFacetObstacleDrawable(Point a, Point b, Point c)
            throws NotPerpendicularException {
        super(a, b, c);
    }

    @Override
    public void paint(UIComponent uiComponent) {
        // not implemented for the moment
    }

}
