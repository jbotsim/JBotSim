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
package io.jbotsim.contrib.obstacle.core;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

/**
 * Interface for all the obstacles (3D obstacles and 2D obstacles)
 * @author mbarjon
 *
 */
public interface Obstacle {

    /**
     * This function is automatically call by the obstacleDetector to known if the obstacle obstructs the link between node1 and node2
     * @param node1 First node of the link
     * @param node2    Second node of the link
     * @return true if the obstacle obstructs the link false if not
     */
    boolean obstructLink(Node node1,Node node2);
    
//    /**
//     * This function return the minimum distance between the obstacle and the given node
//     * @param node The node which you want to know is minimum distance from the obstacle
//     * @return The minimum distance between the obstacle and the node
//     */
//    public double minimumDistance(Node node);
    
    /**
     * This function return the Point on the obstacle which is at the minimum distance from the given node
     * @param node The node which you want to know the point on the obstacle which is at the minimum distance from it
     * @return The point on the obstacle which is at the minimum distance from the node
     */
    Point pointAtMinimumDistance(Node node);

}
