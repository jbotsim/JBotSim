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

package io.jbotsim.contrib.messaging;

import io.jbotsim.core.Topology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class AsyncMessageEngineTest {

    protected static final int NB_OCCURRENCES = 5000000;

    protected static final int MINIMUM_VALUE = 0;
    protected static final int LAMBDA = AsyncMessageEngine.DEFAULT_AVERAGE_DURATION;

    private AsyncMessageEngine messageEngine;

    @BeforeEach
    void setUp() {
        messageEngine = new AsyncMessageEngine(new Topology());
    }

    @Test
    void computeDelayFunction_minOk() {

        boolean minReached = false;
        int minCount = 0;
        int lesserCount = 0;
        int maxValue = 0;

        for(int i = 0; i < NB_OCCURRENCES; i++) {

            int current = messageEngine.computeDelayFunction(LAMBDA);
            assertTrue(current>= MINIMUM_VALUE);

            if(current == MINIMUM_VALUE) {
                minReached = true;
                minCount++;
            }
            if(current <= LAMBDA)
                lesserCount++;
            if(current > maxValue)
                maxValue = current;
        }
        assertTrue(minReached);
//        System.out.println("minCount "+ (double)minCount/NB_OCCURRENCES*100 +"%");
//        System.out.println("lesserCount "+ (double)lesserCount/NB_OCCURRENCES*100 +"%");
//        System.out.println("maxValue "+ maxValue);
    }
    
    
    @Test
    void computeDelayFunction_meansOk() {
        for(int lambda = 1; lambda < 200; lambda+=25)
            testMean(lambda);
    }

    private void testMean(int lambda) {
        double value = 0;
        for(int i = 0; i < NB_OCCURRENCES; i++)
            value += messageEngine.computeDelayFunction(lambda);

        double actualMean =  value / NB_OCCURRENCES;
        double diff = Math.abs(lambda - actualMean);
        double lambdaPercentage = lambda * 0.05;
//        System.out.println("lambda "+ lambda +", actualMean " + actualMean +
//                ", diff " + diff + ", percentage " + lambdaPercentage);
        assertTrue(diff < lambdaPercentage);
    }
}