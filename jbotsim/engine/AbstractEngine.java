/*******************************************************************************
 * This file is part of JBotSim.
 * 
 *     JBotSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JBotSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JBotSim.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     contributors:
 *     Arnaud Casteigts
 *******************************************************************************/
package jbotsim.engine;

import jbotsim.core.Topology;
import jbotsim.engine.event.ClockListener;

public abstract class AbstractEngine implements ClockListener{
	protected Topology topo;
	protected boolean running=true;
	
	public AbstractEngine(Topology topo, int clockRate) {
		this.topo=topo;
		topo.getClock().addClockListener(this, clockRate);
	}
	public void changeRate(int newClockRate){
		topo.getClock().removeClockListener(this);
		topo.getClock().addClockListener(this, newClockRate);
	}
	public abstract void onClock();
	
	public boolean isRunning(){
		return this.running;
	}
	public void setRunning(boolean running){
		this.running=running;
	}
}
