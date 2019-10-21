/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

class TopologyLifeCycleTest {

    Node mockedNode;

    @BeforeEach
    void setUp() {
        mockedNode = spy(new Node());
    }

    private Topology initTopology() {
        Topology topology = new Topology();
        topology.addNode(mockedNode);
        return topology;
    }

    @Test
    void test_none() {
        Topology topology = initTopology();

        CheckParam checkParam = new CheckParam(topology);
        checkState(checkParam);
    }

    @Test
    void test_initialize() {
        Topology topology = initTopology();
        topology.initialize();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkState(checkParam);
    }

    @Test
    void test_start() {
        Topology topology = initTopology();
        topology.start();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkParam.shouldBeStarted = true;
        checkParam.wantedNumberOfOnStart = 1;
        checkParam.shouldBeRunning = true;
        checkState(checkParam);
    }

    @Test
    void test_initializeAndStart() {
        Topology topology = initTopology();
        topology.initialize();
        topology.start();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkParam.shouldBeStarted = true;
        checkParam.wantedNumberOfOnStart = 1;
        checkParam.shouldBeRunning = true;
        checkState(checkParam);
    }

    @Test
    void test_initializeAndStartAndPause() {
        Topology topology = initTopology();
        topology.initialize();
        topology.start();
        topology.pause();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkParam.shouldBeStarted = true;
        checkParam.wantedNumberOfOnStart = 1;
        checkParam.shouldBeRunning = false;
        checkState(checkParam);
    }

    @Test
    void test_initializeAndStartAndPauseAndResume() {
        Topology topology = initTopology();
        topology.initialize();
        topology.start();
        topology.pause();
        topology.resume();


        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkParam.shouldBeStarted = true;
        checkParam.wantedNumberOfOnStart = 1;
        checkParam.shouldBeRunning = true;
        checkState(checkParam);
    }

    @Test
    void test_restart() {
        Topology topology = initTopology();
        topology.restart();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkParam.shouldBeStarted = true;
        checkParam.wantedNumberOfOnStart = 1;
        checkParam.shouldBeRunning = true;
        checkState(checkParam);
    }

    @Test
    void test_restartAndRestart() {
        Topology topology = initTopology();
        topology.restart();
        topology.restart();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.shouldBeInitialized = true;
        checkParam.wantedNumberOfOnInit = 1;
        checkParam.shouldBeStarted = true;
        checkParam.wantedNumberOfOnStart = 2;
        checkParam.shouldBeRunning = true;
        checkState(checkParam);
    }

    @Test
    void test_nodeRemoval() {
        Topology topology = initTopology();
        topology.removeNode(mockedNode);

        CheckParam checkParam = new CheckParam(topology);
        checkParam.wantedNumberOfOnStop = 1;
        checkState(checkParam);
    }

    @Test
    void test_clear() {
        Topology topology = initTopology();
        topology.clear();

        CheckParam checkParam = new CheckParam(topology);
        checkParam.wantedNumberOfOnStop = 1;
        checkState(checkParam);
    }

    private void checkState(CheckParam checkParam) {
        assertEquals(checkParam.shouldBeInitialized, checkParam.topology.isInitialized());
        verify(mockedNode, times(checkParam.wantedNumberOfOnInit)).onInit();
        assertEquals(checkParam.shouldBeStarted, checkParam.topology.isStarted());
        verify(mockedNode, times(checkParam.wantedNumberOfOnStart)).onStart();
        assertEquals(checkParam.shouldBeRunning, checkParam.topology.isRunning());
        verify(mockedNode, times(checkParam.wantedNumberOfOnStop)).onStop();
    }

    private static class CheckParam {
        Topology topology;
        boolean shouldBeInitialized = false;
        int wantedNumberOfOnInit = 0;
        boolean shouldBeStarted = false;
        public boolean shouldBeRunning = false;
        int wantedNumberOfOnStart = 0;
        public int wantedNumberOfOnStop = 0;

        public CheckParam(Topology topology) {
            this.topology = topology;
        }

    }
}