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

import java.util.Random;
import java.util.Vector;

/*
 * Edge-Markovian Time-Varying Graph Builder.
 */
public class EMTVGBuilder {
    public static TVG createGraph(Vector<Node> nodes, double birthRate, double deathRate, int lifetime){
        TVG tvg=new TVG();
        double steadyProb = birthRate/(birthRate+deathRate);
        for (Node n : nodes)
            tvg.nodes.add(n);
        createInitialEdges(tvg, steadyProb);
        for (int date=1; date<lifetime-1; date++)
            createNextEdges(tvg, date, birthRate, deathRate);
        createLastEdges(tvg, lifetime-1);
        return tvg;
    }
    private static void createInitialEdges(TVG tvg, double steadyProb){
        Random r=new Random();
        Vector<Node> nodes=tvg.nodes;
        for (int i=0; i<nodes.size(); i++){
            for (int j=i+1; j<nodes.size(); j++){
                TVLink l=new TVLink(nodes.elementAt(i), nodes.elementAt(j));
                tvg.tvlinks.add(l);
                if (r.nextDouble()<steadyProb)
                    l.appearanceDates.add(0);
            }
        }        
    }
    private static void createNextEdges(TVG tvg, int date, double birthRate, double deathRate){
        Random r=new Random();
        for (TVLink l : tvg.tvlinks){
            if (l.isPresentAtTime(date-1)){
                if (r.nextDouble()<deathRate)
                    l.disappearanceDates.add(date);
            }else{
                if (r.nextDouble()<birthRate)
                    l.appearanceDates.add(date);
            }
        }
    }
    private static void createLastEdges(TVG tvg, int date){
        for (TVLink l : tvg.tvlinks)
            if (l.isPresentAtTime(date) && !l.isPresentAtTime(0))
                l.disappearanceDates.add(date);
    }
}
