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
package io.jbotsim.contrib.obstacle.core;

import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the plugin used to store (or remove) the obstacles and to register (or unregister) the obstacle's listener.
 * @author mbarjon
 *
 */

public class ObstacleManager  {
    
    //private static final HashMap<Topology,ObstacleDetector> map = new HashMap<>();
    
    protected ObstacleManager(){
        
    }

    /**
     * Constructor of the class, replace the default class used to check the links by the class that checks if an obstacle prohibit a link.
     * @param topology The Topology that will contains the obstacles.
     */

    public static void init(Topology topology) {
        topology.setLinkResolver(new ObstacleLinkResolver(topology));
        topology.setProperty("Obstacles", new ArrayList<Obstacle>());
        topology.setProperty("obstaclelisteners", new ArrayList<ObstacleListener>());
        topology.setProperty("obstacleDetector",new ObstacleDetector(topology));
    }
    
    /**
     * Add the given obstacle into the topology
     * @param obstacle The obstacle that will be added to the topology
     * @param topology The topology
     */
    @SuppressWarnings("unchecked")
    public static void addObstacle(Obstacle obstacle, Topology topology){
        List<Obstacle> tmp=(ArrayList<Obstacle>)topology.getProperty("Obstacles");
        tmp.add(obstacle);
    }
    
    /**
     * List the obstacles that are present inside the topology
     * @param topology The topology
     * @return A copy of the list of obstacles
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Obstacle> getObstacles(Topology topology){
        return new ArrayList<>((ArrayList<Obstacle>)topology.getProperty("Obstacles"));
    }
    
    /**
     * Remove the given obstacle from the topology if the obstacle exist in the topology or do nothing
     * @param obstacle The {@link Obstacle} that will be removed from the topology
     * @param topology The {@link Topology} containing the obstacle
     */
    @SuppressWarnings("unchecked")
    public static void removeObstacle(Obstacle obstacle, Topology topology){
        List<Obstacle> tmp=(ArrayList<Obstacle>)topology.getProperty("Obstacles");
        tmp.remove(obstacle);
    }
    
    public static ObstacleDetector getObstacleDetector(Topology topology){
        return (ObstacleDetector) topology.getProperty("obstacleDetector");
    }

    public static void addObstacleListener(ObstacleListener listener, Topology topology){
        List<ObstacleListener> obstacleListeners = (List<ObstacleListener>) topology.getProperty("obstaclelisteners");
            obstacleListeners.add(listener);
    }

    public static void removeObstacleListener(ObstacleListener listener, Topology topology){
        List<ObstacleListener> obstacleListeners = (List<ObstacleListener>) topology.getProperty("obstaclelisteners");
        obstacleListeners.remove(listener);
    }
}
