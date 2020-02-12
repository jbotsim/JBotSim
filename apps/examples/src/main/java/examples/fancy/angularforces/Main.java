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

package examples.fancy.angularforces;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

/**
 * Created by Arnaud Casteigts on 15/03/17.
 */
public class Main {
    public static void main(String args[]) throws Exception{
        Topology tp=new Topology(400,300);
        tp.setCommunicationRange(70);
        Forces.Dth=tp.getCommunicationRange()*0.851;
        tp.setSensingRange(Forces.Dth / 2);
        tp.setDefaultNodeModel(Robot.class);
        tp.setTimeUnit(6);
        new JViewer(tp);
        tp.start();
    }
}
