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

public abstract class Clock {
    ClockManager manager;

    public Clock(ClockManager manager) {
        this.manager = manager;
    }

    /**
     * Returns the time unit of the clock, in milliseconds.
     */
    abstract public int getTimeUnit();

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     * @param delay The desired time unit (1 corresponds to the fastest rate)
     */
    abstract public void setTimeUnit(int delay);

    /**
     * Indicates whether the clock is currently running or paused.
     * @return <tt>true</tt> if running, <tt>false</tt> if paused.
     */
    abstract public boolean isRunning();

    /**
     * Starts the clock.
     */
    abstract public void start();

    /**
     * Pauses the clock.
     */
    abstract public void pause();

    /**
     * Resumes the clock if it was paused.
     */
    abstract public void resume();
}
