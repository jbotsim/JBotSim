/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim;

import jbotsim.event.ClockListener;
import jbotsim.ui.JTopology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

class Clock {
    Topology tp;
    HashMap<ClockListener, Integer> listeners=new HashMap<ClockListener, Integer>();
    HashMap<ClockListener, Integer> countdown=new HashMap<ClockListener, Integer>();
    Integer time=0;

    Clock(Topology topology){
        this.tp = topology;

        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    }

    /**
     * Performs a single round
     */
    public void step() {
      // Delivers messages first
      tp.getMessageEngine().onClock();
      // Then give the hand to the nodes
      tp.getNodeScheduler().onClock(tp);
      // Then to the topology itself
      tp.onClock();
      // Finally, to all other listeners whose countdown has expired
      for (ClockListener cl : getExpiredListeners()) {
          cl.onClock();
          countdown.put(cl, listeners.get(cl)); // reset countdown
      }
      time++;
    }

    protected ArrayList<ClockListener> getExpiredListeners(){
        ArrayList<ClockListener> expiredListeners = new ArrayList<ClockListener>();
        for(ClockListener cl : listeners.keySet()) {
            Integer count = countdown.get(cl);
            if (count == 1)
                expiredListeners.add(cl);
            else
                countdown.put(cl, count - 1);
        }
        return expiredListeners;
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
     * Returns the current time of the clock in time units.
     */
    public Integer currentTime(){
        return time;
    }
    /**
     * Sets the clock time to 0.
     */
    public void reset(){
        time=0;
    }
}
