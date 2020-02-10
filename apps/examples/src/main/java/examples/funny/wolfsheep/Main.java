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

package examples.funny.wolfsheep;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

/**
 * Created by acasteig on 31/08/16.
 */
public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology(800,600);
        tp.setTimeUnit(20);
        tp.disableWireless();
        tp.pause();
        for (int i = 0; i < 5; i++){
            // 3 randomly located wolves and sheeps.
            tp.addNode(-1, -1, new Wolf());
            tp.addNode(-1, -1, new Sheep());
        }
        new JViewer(tp);
        tp.start();
        tp.pause();
    }
}
