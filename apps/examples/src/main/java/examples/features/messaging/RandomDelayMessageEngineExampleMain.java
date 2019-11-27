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

package examples.features.messaging;

import io.jbotsim.contrib.messaging.RandomDelayMessageEngine;
import io.jbotsim.core.MessageEngine;
import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyGenerators;
import io.jbotsim.ui.JViewer;

public class RandomDelayMessageEngineExampleMain {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setTimeUnit(10);
        tp.setDefaultNodeModel(GridMessagingNode.class);
        TopologyGenerators.generateGrid(tp, 10,10);
        new JViewer(tp);

        MessageEngine messageEngine = new RandomDelayMessageEngine(tp, 10);
        tp.setMessageEngine(messageEngine);

        tp.start();
    }
}
