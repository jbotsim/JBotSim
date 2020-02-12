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

import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.contrib.obstacle.core.Obstacle;
import io.jbotsim.contrib.obstacle.core.ObstacleManager;
import io.jbotsim.ui.painting.UIComponent;


/**
    ObstaclePainter is a class used to represent the obstacles on the JViewer and it inherited from JTopology
    @author mbarjon
    
 */
public class ObstaclePainter implements BackgroundPainter {
    
    private static final long serialVersionUID = 561583583776975365L;

    /**
         Build the ObstaclePainter
     */
    public ObstaclePainter() {
    }

    /**
        The methods is automatically call by the JViewer and draw all the nodes and all the obstacles
     */

    @Override
    public void paintBackground(UIComponent uiComponent, Topology topology) {
        for(Obstacle o : ObstacleManager.getObstacles(topology)){
            if(o instanceof ObstacleDrawable) {
                ObstacleDrawable drawable = (ObstacleDrawable) o;
                drawable.paint(uiComponent);
            }
        }
    }
}
