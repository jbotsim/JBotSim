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
package jbotsim;

import jbotsim.event.ClockListener;
import jbotsim.ui.JTopology;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;


class Clock {
	TreeMap<ClockListener, Integer> listeners;
	HashMap<ClockListener, Integer> countdown=new HashMap<ClockListener, Integer>();
	Timer timer=new Timer(10, new ActionHandler());
	Integer time=0;

	Clock(){
        listeners = new TreeMap<ClockListener, Integer>(new ListenerComparator());
		timer.start();
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
	}
	private class ListenerComparator implements Comparator<ClockListener>{
		public int compare(ClockListener arg0, ClockListener arg1) {
			if (arg0 instanceof MessageEngine && !(arg1 instanceof MessageEngine))
				return -1;
			if (!(arg0 instanceof MessageEngine) && arg1 instanceof MessageEngine)
				return 1;
			if (arg0 instanceof Node && !(arg1 instanceof Node))
				return -1;
			if (!(arg0 instanceof Node) && arg1 instanceof Node)
				return 1;
			if (arg0 instanceof Node && arg1 instanceof Node)
				return (new Integer(arg0.hashCode())).compareTo(arg1.hashCode());
            if (arg0 instanceof Topology && !(arg1 instanceof Topology))
                return -1;
            if (!(arg0 instanceof Topology) && arg1 instanceof Topology)
                return 1;
			return (((Integer)arg0.hashCode()).compareTo(arg1.hashCode()));
		}
	}
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent evt) {
            ArrayList<ClockListener> expiredListeners = new ArrayList<ClockListener>();
            ArrayList<ClockListener> expiredNodes = new ArrayList<ClockListener>();
			for(ClockListener cl : new ArrayList<ClockListener>(listeners.keySet())) {
                Integer I = countdown.get(cl);
                if (I != null) {
                    if (I == 1) {
                        expiredListeners.add(cl);
                        if (cl instanceof Node)
                            expiredNodes.add(cl);
                        countdown.put(cl, listeners.get(cl));
                    } else {
                        countdown.put(cl, I - 1);
                    }
                }
            }
            for (ClockListener cl : expiredNodes)
                ((Node) cl).onPreClock();
            for (ClockListener cl : expiredListeners)
                cl.onClock();
            for (ClockListener cl : expiredNodes)
                ((Node) cl).onPostClock();
			time++;
		}
	}
	/**
	 * Registers the specified listener to the events of the clock.
	 * @param listener The listener to register.
	 * @param period The desired period between consecutive onClock() events,
	 * in time units.
	 */
	public void addClockListener(ClockListener listener, int period){
		listeners.put(listener, period);
		countdown.put(listener, period);
	}
	/**
	 * Registers the specified listener to every pulse of the clock.
	 * @param listener The listener to register.
	 */
	public void addClockListener(ClockListener listener){
		listeners.put(listener, 1);
		countdown.put(listener, 1);
	}
	/**
	 * Unregisters the specified listener. (The <tt>onClock()</tt> method of this 
	 * listener will not longer be called.) 
	 * @param listener The listener to unregister.
	 */
	public void removeClockListener(ClockListener listener){
		listeners.remove(listener);
		countdown.remove(listener);
	}
	/**
	 * Returns the time unit of the clock, in milliseconds.
	 */
	public int getTimeUnit(){
		return timer.getDelay();
	}
	/**
	 * Sets the time unit of the clock to the specified value in millisecond.
	 * @param delay The desired time unit (1 corresponds to the fastest rate)
	 */
	public void setTimeUnit(int delay){
		timer.setDelay(delay);
	}
	/**
	 * Returns the current time of the clock in time units.
	 */
	public Integer currentTime(){
		return time;
	}
	/**
	 * Indicates whether the clock is currently running or paused.
	 * @return <tt>true</tt> if running, <tt>false</tt> if paused.
	 */
	public boolean isRunning(){
		return timer.isRunning();
	}
	/**
	 * Pauses the clock (freezes time and stops to send onClock() events to 
	 * listeners).
	 */
	public void pause(){
		timer.stop();
	}
	/**
	 * Resumes the clock if it was paused. 
	 */
	public void resume(){
		timer.start();
	}
	/** 
	 * Sets the clock time to 0.
	 */
	public void reset(){
		time=0;
	}
}
