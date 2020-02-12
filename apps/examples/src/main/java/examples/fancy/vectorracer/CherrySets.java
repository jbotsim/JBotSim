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

package examples.fancy.vectorracer;

import io.jbotsim.core.Topology;

/**
 * Created by acasteig on 26/01/17.
 */
public class CherrySets {
    public static void distribute(Topology tp){
        for (int i=0; i<20; i++) {
            double x = Math.random() * 600 + 100;
            double y = Math.random() * 400 + 100;
            tp.addNode(x, y, new Cherry());
        }
    }
}
