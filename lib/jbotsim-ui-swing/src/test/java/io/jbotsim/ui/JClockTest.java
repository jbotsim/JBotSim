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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class JClockTest  {

    protected static final int DEFAULT_DELAY = 10;
    private JClock jClock;

    @BeforeEach
    void setUp() {
        jClock = new JClock(null);
    }

    @Test
    void create_usesSwingTimerWithDefaultDelay() {
        assertEquals(Timer.class, jClock.timer.getClass());
        checkDelay(DEFAULT_DELAY);
    }

    private void checkDelay(int delay) {
        checkJClockTimeUnitDefault(jClock, delay);
        checkSwingTimerDelays(jClock.timer, delay);
    }

    private void checkSwingTimerDelays(Timer timer, int expectedDelay) {
        assertEquals(expectedDelay, timer.getDelay());
        assertEquals(expectedDelay, timer.getInitialDelay());
    }

    private void checkJClockTimeUnitDefault(JClock jClock, int expected) {
        assertEquals(expected, jClock.getTimeUnit());
    }

    @Test
    void timeUnitModification_takenIntoAccount() {
        jClock.setTimeUnit(50);
        checkDelay(50);
    }

}