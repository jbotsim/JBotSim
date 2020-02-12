/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.core;

/**
 * <p>The abstract class {@link Clock} is used by the {@link ClockManager} to provide JBotSim with a clock mechanism.</p>
 *
 * <p>A {@link DefaultClock} is provided, but each platform/implementation is able to use its own clock by subclassiong
 * the {@link Clock} class.</p>
 */
public abstract class Clock {
    protected ClockManager manager;

    public Clock(ClockManager manager) {
        this.manager = manager;
    }

    /**
     * <p>Returns the time unit of the clock, in milliseconds. The time unit is the duration of a round.</p>
     * @return the time unit of the clock, in milliseconds.
     */
    public abstract int getTimeUnit();

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     *
     * @param timeUnit The desired time unit
     */
    public abstract void setTimeUnit(int timeUnit);

    /**
     * Indicates whether the clock is currently running or paused.
     *
     * @return <code>true</code> if running, <code>false</code> if paused.
     */
    public abstract boolean isRunning();

    /**
     * Starts the clock.
     */
    public abstract void start();

    /**
     * Pauses the clock.
     */
    public abstract void pause();

    /**
     * Resumes the clock if it was paused.
     */
    public abstract void resume();
}
