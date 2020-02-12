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

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.Vector;

public class EMTVGBuilderTest {
    public static void main(String args[]){
        Vector<Node> nodes=new Vector<Node>();
        Node n1=new Node(); n1.setLocation(100, 100); nodes.add(n1);
        Node n2=new Node(); n2.setLocation(100, 200); nodes.add(n2);
        Node n3=new Node(); n3.setLocation(200, 200); nodes.add(n3);
        Node n4=new Node(); n4.setLocation(200, 100); nodes.add(n4);
        TVG tvg= EMTVGBuilder.createGraph(nodes, 0.1, 0.1, 20);
        System.out.println(tvg);
        Topology tp=new Topology();
        tp.setTimeUnit(100);
        new JViewer(tp);
        TVGPlayer player=new TVGPlayer(tvg, tp, 20);
        player.start();
    }
}
