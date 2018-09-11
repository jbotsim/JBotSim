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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClockManager {
    Topology tp;
    HashMap<ClockListener, Integer> listeners = new HashMap<>();
    HashMap<ClockListener, Integer> countdown = new HashMap<>();
    Class<? extends Clock> clockModel = null;
    Clock clock = null;
    Integer time = 0;
    Integer timeUnit = 10; // duration of a round in ms
    int nbPauses = 0;

    ClockManager(Topology topology) {
        this.tp = topology;
    }

    public void onClock() {
        List<ClockListener> expiredListeners = new ArrayList<>();
        for (ClockListener cl : listeners.keySet()) {
            countdown.put(cl, countdown.get(cl) - 1);
            if (countdown.get(cl) == 0) {
                expiredListeners.add(cl);
                countdown.put(cl, listeners.get(cl));
            }
        }
        tp.getScheduler().onClock(tp, expiredListeners);
        time++;
    }

    /**
     * Returns a reference to the Clock.
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Returns the clock model currently in use.
     */
    public Class<? extends Clock> getClockModel() {
        return clockModel;
    }

    /**
     * Sets the clock model (to be instantiated automatically).
     *
     * @param clockModel A class that extends JBotSim's abstract Clock
     */
    public void setClockModel(Class<? extends Clock> clockModel) {
        this.clockModel = clockModel;
    }

    /**
     * Registers the specified listener to the events of the clock.
     *
     * @param listener The listener to register.
     * @param period   The desired period between consecutive onClock() events,
     *                 in time units.
     */
    public void addClockListener(ClockListener listener, int period) {
        listeners.put(listener, period);
        countdown.put(listener, period);
    }

    /**
     * Registers the specified listener to every pulse of the clock.
     *
     * @param listener The listener to register.
     */
    public void addClockListener(ClockListener listener) {
        listeners.put(listener, 1);
        countdown.put(listener, 1);
    }

    /**
     * Unregisters the specified listener. (The <tt>onClock()</tt> method of this
     * listener will not longer be called.)
     *
     * @param listener The listener to unregister.
     */
    public void removeClockListener(ClockListener listener) {
        listeners.remove(listener);
        countdown.remove(listener);
    }

    /**
     * Returns the time unit of the clock, in milliseconds.
     */
    public int getTimeUnit(){
        return timeUnit;
    }

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     *
     * @param timeUnit The desired time unit
     */
    public void setTimeUnit(int timeUnit){
        this.timeUnit = timeUnit;
        if (clock != null)
            clock.setTimeUnit(timeUnit);
    }

    /**
     * Returns the current round number.
     */
    public Integer currentTime() {
        return time;
    }

    /**
     * Starts the clock (if no model is set, DefaultClock is used).
     */
    public void start() {
        if (clockModel == null) {
            try {
                clockModel = (Class<? extends Clock>) Class.forName("jbotsim.DefaultClock");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            Constructor<? extends Clock> c = clockModel.getConstructor(ClockManager.class);
            clock = c.newInstance(this);
            clock.setTimeUnit(timeUnit);
            clock.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Indicates whether the clock is currently running or paused.
     *
     * @return <tt>true</tt> if running, <tt>false</tt> if paused or not started.
     */
    public boolean isRunning() {
        if (clock != null)
            return clock.isRunning();
        else
            return false;
    }

    /**
     * Pauses the clock (or increments the pause counter).
     */
    public void pause() {
        if (clock != null) {
            if (nbPauses == 0)
                clock.pause();
            nbPauses++;
        }
    }

    /**
     * Resumes the clock (or decrements the pause counter).
     */
    public void resume() {
        if (clock != null) {
            if (nbPauses > 0) {
                nbPauses--;
                if (nbPauses == 0)
                    clock.resume();
            }
        }
    }

    /**
     * Sets the clock time to 0.
     */
    public void reset() {
        time = 0;
    }
}
