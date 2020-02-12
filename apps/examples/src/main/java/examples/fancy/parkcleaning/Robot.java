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

import io.jbotsim.core.Node;
import io.jbotsim.core.event.ClockListener;

import io.jbotsim.core.Point;
import java.util.List;

public class Robot extends Node implements ClockListener{
	protected Point target;
	double step = 1;
	
	public Robot(){
		setIcon("/examples/fancy/parkcleaning/gmrobot.png");
		setIconSize(10);
		setSensingRange(30);
		disableWireless();
	}		

	public void onClock() {
		if (target != null){
			if (distance(target) > step){
				setDirection(target);
				move(step);
			}else target=null;
		}
		List<Node> sensedObjects = this.getSensedNodes();
		for (Node thing : sensedObjects)
			if (thing instanceof Garbage && thing.getColor()==getColor())
				getTopology().removeNode(thing);
	}
}
