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
package io.jbotsim.contrib.messaging;

import io.jbotsim.core.Message;
import io.jbotsim.core.MessageEngine;
import io.jbotsim.core.Topology;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * <p>The {@link RandomMessageEngine} is a random alternative to JBotSim's default {@link MessageEngine}.</p>
 * <p>For each new message, a random delay is drawn in [0,{@link #getDelay()}).</p>
 */
public class RandomMessageEngine extends MessageEngine {
    protected Random r = new Random();

    /**
     * <p>Creates a {@link RandomMessageEngine} object.</p>
     *
     * @param topology the {@link Topology} to use.
     * @param maxDelay the maximum number of rounds to apply as a delay to the message delivery, as an integer.
     */
    public RandomMessageEngine(Topology topology, int maxDelay){
        super(topology, maxDelay);
        assert(maxDelay > 0);
    }

    @Override
    protected int getDelayForMessage(Message message) {
        return r.nextInt(getDelay());
    }
}
