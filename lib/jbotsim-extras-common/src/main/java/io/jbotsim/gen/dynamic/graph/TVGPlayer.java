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
package io.jbotsim.gen.dynamic.graph;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;

public class TVGPlayer implements ClockListener{
    protected TVG tvg;
    protected Topology tp;
    protected Integer period=null;
    /**
     * Plays the specified time-varying graph on the specified topology.
     * @param tvg the time-varying graph
     * @param tp the target topology
     */
    public TVGPlayer (TVG tvg, Topology tp){
        this(tvg, tp, null);
    }
    /**
     * Plays forever the specified time-varying graph on the specified topology
     * by taking all dates modulo the specified period.
     * @param tvg the time-varying graph
     * @param tp the target topology
     * @param period the period after which the player should be restarted
     */
    public TVGPlayer (TVG tvg, Topology tp, Integer period){
        this.tvg=tvg;
        this.tp=tp;
        this.period=period;
        tp.setCommunicationRange(0);
        for (Node n : tvg.nodes)
            tp.addNode(n);
    }
    public void start(){
        tp.resetTime();
        tp.addClockListener(this);
        for (TVLink l : tvg.tvlinks)
            if (l.appearanceDates.contains(0))
                //tp.addLink(l, true); // add silently.. (nodes not notified)
                tp.addLink(l);
    }
    public void onClock(){
        updateLinks();
    }
    protected void updateLinks(){
        Integer time = (period==null) ? tp.getTime() : tp.getTime() % period;
        for (TVLink l : tvg.tvlinks){
            if (l.disappearanceDates.contains(time) && tp.getLinks().contains(l))
                tp.removeLink(l);
            if (l.appearanceDates.contains(time) && !tp.getLinks().contains(l))
                tp.addLink(l);
        }        
    }
}
