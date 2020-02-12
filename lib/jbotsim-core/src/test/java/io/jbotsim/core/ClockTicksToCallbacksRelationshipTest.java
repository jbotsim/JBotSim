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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <p>These tests enforce basic behaviours linking the number of clock ticks to:</p>
 * <ul>
 *     <li>the value returned by {@link ClockManager#currentTime()}</li>
 *     <li>the number of time some of the main callbacks (onStart(), onPreClock(), onClock(), onPostClock()) have been
 *     called</li>
 * </ul>
 */
class ClockTicksToCallbacksRelationshipTest {
    MockNode mockedNode;
    Topology topology;

    @BeforeEach
    void setUp() {
        mockedNode = spy(new MockNode());
        topology = initializeTopology();
    }

    private Topology initializeTopology() {
        Topology topology = new Topology();
        topology.setDefaultNodeModel(MockNode.class);
        topology.setClockModel(MockedClock.class);
        topology.addNode(mockedNode);
        return topology;
    }

    @Test
    void beforeStart_roundZero_noCallbackCalled() {

        checkTime(0);
        checkOnStart(0, -1);
        checkOnClocks(0, -1);

    }


    @Test
    void firstClockTick_roundZero_callbacksCalledOnce() {

        int expectedTime = 0;
        int expectedNbCalls = 0;

        topology.start();

        checkStartedCase(expectedTime, expectedNbCalls);

        fireMockedClock();
        expectedNbCalls++;
        checkStartedCase(expectedTime, expectedNbCalls);

    }

    @Test
    void hundredClockTicks_roundsIncrement_onStartOnce_onClocksIncrement() {

        int expectedTime = 0;
        int expectedNbCalls = 0;

        topology.start();

        fireMockedClock();
        expectedNbCalls++;
        checkStartedCase(expectedTime, expectedNbCalls);


        for (int i = 1; i < 100; i++) {
            fireMockedClock();
            expectedTime++;
            expectedNbCalls++;
            checkStartedCase(expectedTime, expectedNbCalls);
        }
    }

    private void checkTime(int expectedTime) {
        assertEquals(expectedTime, topology.getTime());
    }

    private void checkOnClocks(int expectedNbCalls, int expectedTimeValue) {
        verify(mockedNode, times(expectedNbCalls)).onPreClock();
        verify(mockedNode, times(expectedNbCalls)).onClock();
        verify(mockedNode, times(expectedNbCalls)).onPostClock();

        if(expectedNbCalls > 0) {
            assertEquals(expectedTimeValue, mockedNode.lastPreClockTime);
            assertEquals(expectedTimeValue, mockedNode.lastClockTime);
            assertEquals(expectedTimeValue, mockedNode.lastPostClockTime);
        }
    }

    private void checkOnStart(int expectedNbCalls, int expectedTimeValue) {
        verify(mockedNode, times(expectedNbCalls)).onStart();

        if(expectedNbCalls > 0)
            assertEquals(expectedTimeValue, mockedNode.lastStartTime);

    }

    private void checkStartedCase(int expectedTime, int expectedNbCalls) {
        checkTime(expectedTime);
        checkOnStart(1, 0);
        checkOnClocks(expectedNbCalls, expectedTime);
    }

    private class MockNode extends Node {
        int lastStartTime = -1;
        int lastPreClockTime = -1;
        int lastClockTime = -1;
        int lastPostClockTime = -1;

        @Override
        public void onStart() {
            super.onStart();
            lastStartTime = getTopology().getTime();
        }

        @Override
        public void onPreClock() {
            super.onPreClock();
            lastPreClockTime = getTopology().getTime();
        }
        @Override
        public void onClock() {
            super.onClock();
            lastClockTime = getTopology().getTime();
        }
        @Override
        public void onPostClock() {
            super.onPostClock();
            lastPostClockTime = getTopology().getTime();
        }
    }


    private void fireMockedClock() {
        ((MockedClock) topology.clockManager.clock).fireClock();
    }

    static public class MockedClock extends Clock {

        public void fireClock() {
            manager.onClock();
        }

        public MockedClock(ClockManager manager) {
            super(manager);
        }

        @Override
        public int getTimeUnit() {
            return 0;
        }

        @Override
        public void setTimeUnit(int timeUnit) {

        }

        @Override
        public boolean isRunning() {
            return false;
        }

        @Override
        public void start() {
//            fireClock();
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }
    }
}