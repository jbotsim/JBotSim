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
package io.jbotsim.contrib.obstacle.ui;

import io.jbotsim.core.Topology;
import io.jbotsim.contrib.obstacle.core.ObstacleManager;
import io.jbotsim.ui.JTopology;

/**
 * Drawable version of the Main class of the plugin (ObstacleManager).
 * @author mbarjon
 *
 */

public class ObstacleManagerDrawable extends ObstacleManager {


    public static void init(Topology topology, JTopology jTopology) {
        init(topology);
        jTopology.addBackgroundPainter(new ObstaclePainter());
    }
    
}
