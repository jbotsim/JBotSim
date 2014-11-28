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

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jbotsim.event.ClockListener;


public class Clock{
	private static Clock clock=new Clock();
	private static TreeMap<ClockListener, Integer> listeners=
		new TreeMap<ClockListener, Integer>(clock.listenerComparator);
	private static HashMap<ClockListener, Integer> countdown=new HashMap<ClockListener, Integer>();
	private Timer timer=new Timer(10, new ActionHandler());
	private ListenerComparator listenerComparator=new ListenerComparator();
	private Integer time=0;
	
	private Clock(){
		timer.start();
	}
	private class ListenerComparator implements Comparator<ClockListener>{
		public int compare(ClockListener arg0, ClockListener arg1) {
			if (arg0 instanceof Message.MessageEngine && !(arg1 instanceof Message.MessageEngine))
				return -1;
			if (!(arg0 instanceof Message.MessageEngine) && arg1 instanceof Message.MessageEngine)
				return 1;
			if (!(arg0 instanceof Node) && arg1 instanceof Node)
				return -1;
			if (arg0 instanceof Node && !(arg1 instanceof Node))
				return 1;
			if (arg0 instanceof Node && arg1 instanceof Node)
				return (new Integer(arg0.hashCode())).compareTo(arg1.hashCode());
				//return (arg0.toString()+((Integer)arg0.hashCode()).toString())
				//.compareTo(arg1.toString()+((Integer)arg1.hashCode()).toString());
			return (((Integer)arg0.hashCode()).compareTo(arg1.hashCode()));
		}		
	}
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent evt) {
			time++;
			for(ClockListener cl : new ArrayList<ClockListener>(listeners.keySet())){
				Integer I=countdown.get(cl);
				if(I==1){
					try{
						if(((Node)cl).topo!=null)
							cl.onClock();
					}catch(Exception e){cl.onClock();};
					countdown.put(cl, listeners.get(cl));
				}else{
					countdown.put(cl, I-1);
				}	
			}	
		}
	}
	/**
	 * Registers the specified listener to the events of the clock. If the 
	 * listener is a <tt>Node</tt>, it is notified only once added to a 
	 * <tt>Topology</tt>. 
	 * @param listener The listener to register.
	 * @param period The desired period between consecutive onClock() events, 
	 * in time units.
	 */
	public static void addClockListener(ClockListener listener, int period){
		listeners.put(listener, period);
		countdown.put(listener, period);
	}
	/**
	 * Unregisters the specified listener. (The <tt>onClock()</tt> method of this 
	 * listener will not longer be called.) 
	 * @param listener The listener to unregister.
	 */
	public static void removeClockListener(ClockListener listener){
		listeners.remove(listener);
		countdown.remove(listener);
	}
	/**
	 * Returns the time unit of the clock, in milliseconds.
	 */
	public static int getTimeUnit(){
		return clock.timer.getDelay();
	}
	/**
	 * Sets the time unit of the clock to the specified value in millisecond.
	 * @param delay The desired time unit (1 corresponds to the fastest rate)
	 */
	public static void setTimeUnit(int delay){
		clock.timer.setDelay(delay);
	}
	/**
	 * Returns the current time of the clock in time units.
	 */
	public static Integer currentTime(){
		return clock.time;
	}
	/**
	 * Indicates whether the clock is currently running or paused.
	 * @return <tt>true</tt> if running, <tt>false</tt> if paused.
	 */
	public static boolean isRunning(){
		return clock.timer.isRunning();
	}
	/**
	 * Pauses the clock (freezes time and stops to send onClock() events to 
	 * listeners).
	 */
	public static void pause(){
		clock.timer.stop();
	}
	/**
	 * Resumes the clock if it was paused. 
	 */
	public static void resume(){
		clock.timer.start();
	}
	/** 
	 * Sets the clock time to 0.
	 */
	public static void reset(){
		clock.time=0;
	}
}
