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

package io.jbotsim.core;

import io.jbotsim.ui.JViewer;

/**
 * <p>This class aims at manually checking that a call to {@link Topology#restart()} actually reset pending messages</p>
 * <ul>
 *     <li>start a broadcast wave (by selecting a node)</li>
 *     <li>restart the topology (using right click command)</li>
 *     <li>visually check that the broadcast wave has stopped (no uninformed node should turn RED after the restart)</li>
 * </ul>
 */
public class MessageEngineTest {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.setDefaultNodeModel(MyBroadcastNode.class);
        topology.setTimeUnit(10);
        DelayMessageEngine messageEngine = new DelayMessageEngine(topology);
        messageEngine.setDelay(200);
        topology.setMessageEngine(messageEngine);
        topology.addNode(50, 50);
        topology.addNode(100, 100);
        topology.addNode(150, 150);
        topology.addNode(200, 200);
        topology.addNode(250, 250);

        new JViewer(topology);
        topology.start();

    }

    public static class MyBroadcastNode extends Node {
        @Override
        public void onStart() {
            super.onStart();
            setColor(null);
        }

        @Override
        public void onSelection() {
            super.onSelection();
            onInformed();
        }

        @Override
        public void onMessage(Message message) {
            super.onMessage(message);
            onInformed();
        }

        private void onInformed() {
            sendAll(new Message());
            setColor(Color.RED);
        }

    }
}
