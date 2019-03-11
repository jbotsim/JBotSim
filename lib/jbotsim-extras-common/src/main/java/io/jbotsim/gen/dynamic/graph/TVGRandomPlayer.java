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

import io.jbotsim.core.Topology;

import java.util.Random;

public class TVGRandomPlayer extends TVGPlayer{
    int timeBound;
    int presenceBound;
    Random rand=new Random();
    
    public TVGRandomPlayer(TVG tvg, Topology tp) {
        this(tvg, tp, 50);
    }
    public TVGRandomPlayer(TVG tvg, Topology tp, int timeBound) {
        this(tvg, tp, 50, 20);
    }
    public TVGRandomPlayer(TVG tvg, Topology tp, int timeBound, int presenceBound) {
        super(tvg, tp);
        this.timeBound=timeBound;
        this.presenceBound=presenceBound;
        for (TVLink l : super.tvg.tvlinks){
            l.setProperty("nextApp", rand.nextInt(timeBound-presenceBound));
            l.setProperty("nextDis", -1);
        }
        updateLinks();
    }
    protected void updateLinks(){
        int now=tp.getTime();
        for (TVLink l : super.tvg.tvlinks){
            int nextApp=(Integer)l.getProperty("nextApp");
            int nextDis=(Integer)l.getProperty("nextDis");
            if (now==nextApp){
                tp.addLink(l);
                l.setProperty("nextDis", now+(rand.nextInt(presenceBound-1))+1);
                l.setProperty("nextApp", now+(rand.nextInt(timeBound-presenceBound-1))+presenceBound+1);
            }else if(now==nextDis){
                tp.removeLink(l);
            }
        }
    }
}
