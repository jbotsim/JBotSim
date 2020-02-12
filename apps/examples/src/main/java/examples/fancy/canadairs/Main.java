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

package examples.fancy.canadairs;

import io.jbotsim.core.*;
import io.jbotsim.ui.JViewer;

/**
 * Created by acasteig on 22/03/15.
 */
public class Main extends LinkResolver {
    static Topology topology;

    @Override
    public boolean isHeardBy(Node n1, Node n2) {
        if ((n1 instanceof Sensor && n2 instanceof Canadair) ||
                (n2 instanceof Sensor && n1 instanceof Canadair))
            return false;
        return (n1.isWirelessEnabled() && n2.isWirelessEnabled()
                && n1.distance(n2) < n1.getCommunicationRange());
    }
    public static void createMap(Topology topology){
        for (int i=0; i<6; i++)
            for (int j=0; j<4; j++)
                topology.addNode(i*100+180-(j%2)*30, j*100+100, new Sensor());
        topology.addNode(50, 400, new Station());
        for (Link link : topology.getLinks())
            link.setColor(Color.gray);
        topology.addNode(50, 500, new Canadair());
        topology.addNode(100, 500, new Canadair());
        topology.addNode(50, 50, new Lake());
    }
    public static void main(String[] args) {
        topology = new Topology(800,600);
        topology.setLinkResolver(new Main());
        DelayMessageEngine messageEngine = new DelayMessageEngine(topology);
        messageEngine.setDelay(10);
        topology.setMessageEngine(messageEngine);
        createMap(topology);
        topology.setTimeUnit(30);
        topology.setDefaultNodeModel(Fire.class);
        new JViewer(topology);
        topology.start();
    }
}
