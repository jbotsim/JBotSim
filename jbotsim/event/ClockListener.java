/*
 * This file is part of JBotSim.
 * 
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts		<casteig@site.uottawa.ca>
 */
package jbotsim.event;

public interface ClockListener {
	/**
	 * Notifies the underlying listener that the clock has reached the desired
	 * number of time steps, repeatedly. This number of steps is to be 
	 * specified during subscription (see <tt>Clock.addClockListener</tt>). 
	 */
	public void onClock();
}
