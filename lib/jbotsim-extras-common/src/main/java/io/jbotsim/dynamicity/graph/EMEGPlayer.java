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
package io.jbotsim.dynamicity.graph;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;

import java.util.Random;

public class EMEGPlayer implements ClockListener{
    protected TVG tvg;
    protected Topology tp;
    protected double birthRate, deathRate, steadyProb;
    /**
     * Plays the specified _demo.J_tvg as an EMEG on the specified topology.
     * @param tvg the time-varying graph
     * @param tp the target topology
     * @param birthRate the desired birth rate
     * @param deathRate the desired death rate
     */
    public EMEGPlayer (TVG tvg, Topology tp, double birthRate, double deathRate){
        this.tvg=tvg;
        this.tp=tp;
        this.birthRate=birthRate;
        this.deathRate=deathRate;
        this.steadyProb = birthRate/(birthRate+deathRate);
        for (Node n : tvg.nodes)
            tp.addNode(n);
    }
    public void start(){
        tp.resetTime();
        tp.addClockListener(this);
        Random r = new Random();
        for (TVLink l : tvg.tvlinks)
            if (r.nextDouble() < steadyProb)
                tp.addLink(l);
    }
    public void onClock(){
        updateLinks();
    }
    protected void updateLinks(){
        Random r = new Random();
        for (TVLink l : tvg.tvlinks){
            if (tp.getLinks().contains(l) && r.nextDouble() < deathRate)
                tp.removeLink(l);
            else if (!tp.getLinks().contains(l) && r.nextDouble() < birthRate)
                tp.addLink(l);
        }        
    }
}
