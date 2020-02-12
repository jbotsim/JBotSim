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
package io.jbotsim.gen.dynamic.graph;

import java.util.List;
import java.util.Random;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

public class EMEGTopology extends Topology{
    private double birthRate;
    private double deathRate;
    public EMEGTopology(double birthRate, double deathRate){
        this.birthRate=birthRate;
        this.deathRate=deathRate;
        setCommunicationRange(0);
    }
    public void initializeEdges() {
        for (Link l : super.getLinks())
            super.removeLink(l);
        Random random=new Random();
        List<Node> nodes = super.getNodes();
        for (int i=0; i<nodes.size(); i++){
            for (int j=i+1; j<nodes.size(); j++){
                Link l=new Link(nodes.get(i), nodes.get(j));
                if (random.nextDouble() < birthRate/(birthRate+deathRate))
                    super.addLink(l);
            }
        }
    }
    public void updateLinks() {
        Random random=new Random();
        List<Node> nodes = super.getNodes();
        for (int i=0; i<nodes.size(); i++){
            for (int j=i+1; j<nodes.size(); j++){
                Link l=new Link(nodes.get(i), nodes.get(j));
                if (super.getLinks().contains(l)){
                    if (random.nextDouble() < deathRate)
                        super.removeLink(l);
                }else{
                    if (random.nextDouble() < birthRate)
                        super.addLink(l);
                }
            }
        }
    }
}
