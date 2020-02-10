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

package examples.fancy.parkcleaning;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class Main {
	public static void main(String[] args) {
		Topology tp = new Topology();
		tp.setNodeModel("UAV", UAV.class);
		tp.setNodeModel("Robot_blue", RobotBlue.class);
		tp.setNodeModel("Robot_red", RobotRed.class);
		tp.setNodeModel("Garbage_blue", GarbageBlue.class);
		tp.setNodeModel("Garbage_red", GarbageRed.class);

		new JViewer(tp);
		new RWP(tp, true);
		tp.start();
	}
}
