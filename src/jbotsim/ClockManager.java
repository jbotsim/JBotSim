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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClockManager{
    Topology tp;
    HashMap<ClockListener, Integer> listeners=new HashMap<ClockListener, Integer>();
    HashMap<ClockListener, Integer> countdown=new HashMap<ClockListener, Integer>();
    Clock clock;
    Integer time = 0;

    ClockManager(Topology topology){
        this.tp = topology;
        clock = new Clock(this);
        clock.start();
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    }

    public void onClock(){
        List<ClockListener> expiredListeners = new ArrayList<>();
        for(ClockListener cl : listeners.keySet()) {
            Integer count = countdown.get(cl);
            if (count == 1)
                expiredListeners.add(cl);
            else
                countdown.put(cl, count - 1);
        }
        tp.getScheduler().onClock(tp, expiredListeners);
        for (ClockListener cl : expiredListeners)
            countdown.put(cl, listeners.get(cl)); // reset countdown
        time++;
    }

    /**
     * Returns a reference to the Clock.
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Sets the Clock.
     * @param clock The Clock to be used.
     */
    public void setClock(Clock clock) {
        if (this.clock != null)
            this.clock.pause();
        this.clock = clock;
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
     * Returns the current round number.
     */
    public Integer currentTime(){
        return time;
    }

    /**
     * Sets the clock time to 0.
     */
    public void reset(){
        time = 0;
    }
}
