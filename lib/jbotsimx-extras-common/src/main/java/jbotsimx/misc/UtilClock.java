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

/**
 * Default Clock is a simple clock implementation using a simple timer to
 * schedule onClock at a fixed rate.
 * This class is not thread-safe
 *
 * Authors:
 * Quentin Bramas <bramas@unistra.fr>
 */

package jbotsimx.misc;

import jbotsim.Clock;
import jbotsim.ClockManager;

import java.util.Timer;
import java.util.TimerTask;

public class UtilClock extends Clock {
    Timer timer;
    long period;
    boolean running;
    DefaultTask task;

    public UtilClock(ClockManager manager) {
        super(manager);
        period = 10;
        running = false;
        task = null;
        timer = new Timer("Default Clock Timer");
    }

    /**
     * TimerTask called by our timer.
     * The task just call the onClock method of the ClockManager
     */
    private class DefaultTask extends TimerTask {
        @Override
        public void run() {
            manager.onClock();
        }
    }

    /**
     * Returns the time unit of the clock, in milliseconds.
     */
    @Override
    public int getTimeUnit() {
        return (int) period;
    }

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     *
     * @param delay The desired time unit (1 corresponds to the fastest rate)
     */
    @Override
    public void setTimeUnit(int delay) {
        period = delay;
        if (running) {
            pause();
            resume();
        }
    }

    /**
     * Indicates whether the clock is currently running or paused.
     *
     * @return <tt>true</tt> if running, <tt>false</tt> if paused.
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts the clock.
     */
    @Override
    public void start() {
        resume();
    }

    /**
     * Pauses the clock.
     */
    @Override
    public void pause() {
        if (!running)
            return;

        running = false;
        task.cancel();
        task = null;
    }

    /**
     * Resumes the clock if it was paused.
     */
    @Override
    public void resume() {
        if (running)
            return;

        running = true;
        task = new DefaultTask();
        timer.scheduleAtFixedRate(task, period, period);
    }
}
