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

import io.jbotsim.core.LinkResolver;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.List;

/**
 * This class override the class WLinkCalculator by checking if the given nodes can communicate. To do this it checks is the nodes are close enough and is it does not exist an obstacle between them.
 * @author mbarjon
 *
 */
public class ObstacleLinkResolver extends LinkResolver {
    
    private Topology topology;
    
    public ObstacleLinkResolver(Topology topology){
        this.topology=topology;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isHeardBy(Node arg0, Node arg1) {
        if( super.isHeardBy(arg0, arg1)){
            for(Obstacle o: ((List<Obstacle>) topology.getProperty("Obstacles"))){
                if(o.obstructLink(arg0, arg1))
                    return false;
            }
            return true;
        }
        return false;
    }
    
}
