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
package io.jbotsim.ui;

import io.jbotsim.core.ClockManager;
import io.jbotsim.core.Clock;

import javax.swing.Timer;

/**
 * <p>The {@link JClock} implements the {@link Clock} interface, using a Swing {@link Timer}.</p>
 */
public class JClock extends Clock {
    Timer timer;

    public JClock(ClockManager manager) {
        super(manager);
        timer = new Timer(10, e -> manager.onClock());
    }

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

    @Override
    public boolean isRunning() {
        return timer.isRunning();
    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void pause() {
        timer.stop();
    }

    @Override
    public void resume() {
        timer.start();
    }
}
