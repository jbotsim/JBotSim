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
package jbotsim.engine.event;

import java.util.Hashtable;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Clock implements ActionListener{ // To replace by ScheduledThreadPoolExecutor
	protected Hashtable<ClockListener, Integer> listeners;
	protected Hashtable<ClockListener, Integer> countdown;
	
	public Clock(){
		listeners=new Hashtable<ClockListener, Integer>();
		countdown=new Hashtable<ClockListener, Integer>();
		(new Timer(1, this)).start();
	}
	public void addClockListener(ClockListener listener, int millisec){
		listeners.put(listener, millisec);
		countdown.put(listener, millisec);
	}
	public void removeClockListener(ClockListener listener){
		listeners.remove(listener);
		countdown.remove(listener);
	}
	public void actionPerformed(ActionEvent evt) {
		for(ClockListener cl : listeners.keySet()){
			Integer I=countdown.get(cl);
			if(I==0){
				cl.onClock();
				countdown.put(cl, listeners.get(cl));
	        }else{
	        	countdown.put(cl, I-1);
	        }	
		}	
	}
}
