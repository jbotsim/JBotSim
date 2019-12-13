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

package io.jbotsim.core;

import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;

/**
 * This class purpose is to check/showcase behaviors linked to the ClockManager
 */
public class ClockManagerTestMain {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.setDefaultNodeModel(ClockNode.class);
        topology.setTimeUnit(2000);

        topology.addClockListener(new MyClockListener(topology));

        topology.addNode(100, 100);

        new JViewer(topology);

//        topology.start();
    }

    public static class ClockNode extends Node {
        public ClockNode() {
//            msg("constructor");
        }

        @Override
        public void onStart() {
            msg("onStart");
        }

        @Override
        public void onStop() {
            msg("onStop");
        }

        @Override
        public void onPreClock() {
            msg("onPreClock");
        }

        @Override
        public void onClock() {
            msg("onClock");
        }

        @Override
        public void onPostClock() {
            msg("onPostClock");
        }

        @Override
        public void onSelection() {
            msg("onSelection");
        }

        private void msg(String methodName) {
            System.out.println("Id=" + getID() + " " + methodName + ": " + getTime());
        }
    }

    private static class MyClockListener implements ClockListener {
        private final Topology topology;

        public MyClockListener(Topology topology) {
            this.topology = topology;
            printTime();
        }

        @Override
        public void onClock() {
            printTime();
        }

        private void printTime() {
            System.out.println("listener.printTime: " + topology.getTime());
        }
    }
}
