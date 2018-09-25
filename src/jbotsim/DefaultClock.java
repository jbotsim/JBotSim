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

package jbotsim;

public class DefaultClock extends Clock implements Runnable{
    volatile boolean running;
    Thread timer;

    public DefaultClock(ClockManager manager) {
        super(manager);
        running = false;
        timer = new Thread(this);
    }

    /**
     * Returns the duration of one time unit, in milliseconds.
     */
    @Override
    public int getTimeUnit() {
        return 0;
    }

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     *
     * @param delay The desired time unit (1 corresponds to the fastest rate)
     */
    @Override
    public void setTimeUnit(int delay) {
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
        running = true;
        timer.start();
    }

    /**
     * Pauses the clock.
     */
    @Override
    public void pause() {
        running = false;
    }

    /**
     * Resumes the clock if it was paused.
     */
    @Override
    public void resume() {
        running = true;
    }

    @Override
    public void run() {
        while (true)
            if (running)
                manager.onClock();
    }
}
