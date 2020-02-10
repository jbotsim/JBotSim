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

public class UAV extends Node{
	public UAV(){
		this.setCommunicationRange(100);
		this.setSensingRange(40);
		this.setIcon("/examples/fancy/parkcleaning/gmuav.png");
		this.setIconSize(20);
		this.setProperty("type", "rwp");
		this.setDirection(2);
	}

	@Override
	public void onSensingIn(Node node) {
		if (node instanceof Garbage && getColor()==null) {
			setProperty("examples/n_parc", node);
			setColor(node.getColor());
		}
		if (node instanceof Robot &&
				node.getColor()==this.getColor() && ((Robot)node).target==null){
			((Robot)node).target = ((Node)this.getProperty("examples/n_parc")).getLocation();
			setColor(null);
		}
	}

}
