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
package io.jbotsim.contrib.obstacle.shapes2d;

import io.jbotsim.core.Point;
import io.jbotsim.contrib.obstacle.ui.ObstacleDrawable;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;


public class CircleObstacleDrawable extends CircleObstacle implements ObstacleDrawable {

    public CircleObstacleDrawable(Point center, double radius) {
        super(center, radius);
    }

    @Override
    public void paint(UIComponent uiComponent) {
        Graphics g = (Graphics) uiComponent.getComponent();
        Color c= g.getColor();
        g.setColor(Color.BLUE);
        g.drawOval((int) (center.getX() - radius), (int) (center.getY() - radius), (int) radius * 2, (int) radius * 2);
        g.setColor(c);
    }


}
