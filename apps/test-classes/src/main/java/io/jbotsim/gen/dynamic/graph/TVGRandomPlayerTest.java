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
import io.jbotsim.gen.dynamic.graph.TVG;
import io.jbotsim.gen.dynamic.graph.TVGRandomPlayer;
import io.jbotsim.ui.JViewer;

public class TVGRandomPlayerTest {
    public static void main(String args[]){
        TVG tvg=new TVG();
        tvg.buildFromFile("/home/arnaud/workspace/code/jbotsim/_testing/dtn/star.tvg");
        Topology tp=new Topology();
        new JViewer(tp);
        tp.resetTime();
        tp.pause();
        tp.setClockSpeed(50);
        TVGRandomPlayer player = new TVGRandomPlayer(tvg, tp, 100);
        player.start();
        tp.resume();
    }
}
