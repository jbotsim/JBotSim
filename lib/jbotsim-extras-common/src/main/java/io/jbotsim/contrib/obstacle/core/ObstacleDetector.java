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

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.MovementListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to check if some obstacle are near of nodes that have say that they want to know it. It do it by checking at each movement of a node that have say it want to know if a obstacle is near. 
 * @author mbarjon
 *
 */
public class ObstacleDetector implements MovementListener {

    //private List<ObstacleListener> obstacleListeners =new ArrayList<>();
    
    private Topology topology;
    
    /**
     * <p>Constructor of the class.</p>
     * <p>It simply registers as a movement listener on the provided {@link Topology}.</p>
     *
     * @param topology the {@link Topology}
     */
    public ObstacleDetector(Topology topology) {
        topology.addMovementListener(this);
        this.topology=topology;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void onMove(Node arg0) {
        List<ObstacleListener> obstacleListeners = (List<ObstacleListener>) topology.getProperty("obstaclelisteners");
        for(ObstacleListener listener:obstacleListeners){
            if(listener==arg0){
                Point n = new Point(arg0.getX(),arg0.getY(),arg0.getZ());
                List<Obstacle> l= new ArrayList<>();
                for(Obstacle o : ((List<Obstacle> )topology.getProperty("Obstacles"))){
                    
                    if(o.pointAtMinimumDistance(arg0).distance(n)< arg0.getSensingRange()){
                        l.add(o);
                    }
                }
                if(! l.isEmpty())
                    listener.onDetectedObstacles(l);
            }
        }
    }
    /*
     * This function store the listener that want to be alarm when an obstacle is near of it
     * @param listener The listener that want to be alarm when an obstacle is near of it
     */
    //public void addObstacleListener(ObstacleListener listener){
    //    obstacleListeners.add(listener);
    //}

    /*
     *  This function remove the listener that do not want anymore to be alarm when an obstacle is near of it
     * @param listener The listener that do not want anymore to be alarm when an obstacle is near of it
     */
    /*public void removeObstacleListener(ObstacleListener listener){
        obstacleListeners.remove(listener);
    }*/
}
