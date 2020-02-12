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

package examples.basic.icons;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class MainIcons {
    public static void main(String[] args) {
        Topology topology = new Topology();

        int nbNodes = 10;
        deployAtCenter(topology, MovingNodeBlueOcean.class, nbNodes);
        deployAtCenter(topology, MovingNodeBlue.class, nbNodes);
        deployAtCenter(topology, MovingNodeFormer.class, nbNodes);
        deployAtCenter(topology, MovingNodePlus.class, nbNodes);
        deployAtCenter(topology, MovingNodeTransparent.class, nbNodes);
        deployAtCenter(topology, MovingNodeDefault.class, nbNodes);

        new JViewer(topology);
        topology.start();
    }

    private static void deployAtCenter(Topology tp, Class nodeClass, int nbNodes) {
        tp.setDefaultNodeModel(nodeClass);
        for(int i = 0; i< nbNodes; i++)
            tp.addNode(tp.getWidth()/2, tp.getHeight()/2);
    }
}
