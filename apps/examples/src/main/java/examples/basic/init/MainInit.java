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

package examples.basic.init;

import io.jbotsim.core.Color;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

/**
 * The {@link MainInit} example shows how {@link Topology#initialize()} allows a {@link Node} to perform computations
 * involving the {@link Topology} before it is started.
 */
public class MainInit {

    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.disableWireless();
        topology.setDefaultNodeModel(MyNode.class);
        topology.setTimeUnit(20);

        topology.addNode(-1, -1);

        new JViewer(topology);
        topology.initialize();
    }

    /**
     * This node:
     * <ul>
     *     <li>moves in a random direction</li>
     *     <li>changes color according to its X location in the Topology</li>
     * </ul>
     */
    public static class MyNode extends Node {
        private boolean topologyKnown;

        @Override
        public void onInit() {
            // After the call to onInit, the Topology is known to the Node, via getTopology()
            topologyKnown = true;

            onMovement();
        }

        @Override
        public void onStart() {
            setDirection(Math.random()*2*Math.PI);
        }

        @Override
        public void onMovement() {
            // The rest of the method will throw a NPE if the Topology has not been initialized
            if(!topologyKnown)
                return;

            double locationRatio = getLocation().getX() / getTopology().getWidth();
            setColor(computeColor(locationRatio));

            System.out.println("[" + getID() + "] - round "  + getTime() + " - " + getLocation() );
        }

        private Color computeColor(double ratio) {
            int grayLevel = (int) (ratio * 255);

            if (grayLevel<0)
                grayLevel = 0;
            if (grayLevel>255)
                grayLevel = 255;

            return new Color(grayLevel, grayLevel, grayLevel);
        }

        @Override
        public void onClock() {
            move();
            wrapLocation();
        }
    }
}
