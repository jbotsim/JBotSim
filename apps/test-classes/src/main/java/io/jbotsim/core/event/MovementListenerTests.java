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

package io.jbotsim.core.event;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class MovementListenerTests {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.setDefaultNodeModel(MyMovingNode.class);
        topology.setTimeUnit(1);

        for (int i=0; i < 2; i++)
            topology.addMovementListener(new MyListener());

        MyMovingNode node = new MyMovingNode();
        topology.addNode(-1, -1, node);

        new JViewer(topology);
        topology.start();

    }

    public static class MyMovingNode extends Node {
        @Override
        public void onMovement() {
            super.onMovement();
        }

        @Override
        public void onClock() {
            super.onClock();
            move();
            wrapLocation();
        }
    }

    private static class MyListener implements MovementListener {
        private int count = 1;
        @Override
        public void onMovement(Node node) {
            count--;

            System.out.println("["+toString()+"]: Node " + node + " has moved");
            if(count <= 0)
                switchListener(node.getTopology());
        }

        private void switchListener(Topology topology) {
            topology.removeMovementListener(this);
            topology.addMovementListener(new MyListener());
        }
    }
}
