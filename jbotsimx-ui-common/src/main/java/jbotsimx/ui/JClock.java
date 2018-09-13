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
package jbotsimx.ui;

import jbotsim.ClockManager;
import jbotsim.Clock;

import javax.swing.Timer;

public class JClock extends Clock {
    Timer timer;

    public JClock(ClockManager manager) {
        super(manager);
        timer = new Timer(10, e -> manager.onClock());
    }

    /**
     * Returns the time unit of the clock, in milliseconds.
     */
    @Override
    public int getTimeUnit() {
        return timer.getDelay();
    }

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     *
     * @param delay The desired time unit (1 corresponds to the fastest rate)
     */
    @Override
    public void setTimeUnit(int delay) {
        timer.setDelay(delay);
    }

    /**
     * Indicates whether the clock is currently running or paused.
     *
     * @return <tt>true</tt> if running, <tt>false</tt> if paused.
     */
    @Override
    public boolean isRunning() {
        return timer.isRunning();
    }

    /**
     * Starts the clock.
     */
    @Override
    public void start() {
        timer.start();
    }

    /**
     * Pauses the clock.
     */
    @Override
    public void pause() {
        timer.stop();
    }

    /**
     * Resumes the clock if it was paused.
     */
    @Override
    public void resume() {
        timer.start();
    }
}
