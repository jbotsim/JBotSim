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

import io.jbotsim.core.*;

import java.util.*;

/**
 * <p>The {@link RandomDelayMessageEngine} is a random alternative to JBotSim's default {@link MessageEngine}.</p>
 * <p>For each new message, a random delay is drawn in [0,{@link #getDelay()}).</p>
 */
public class RandomDelayMessageEngine extends DelayMessageEngine {
    protected Random r = new Random();

    /**
     * <p>Creates a {@link RandomDelayMessageEngine} object.</p>
     *
     * @param topology the {@link Topology} to use.
     * @param maxDelay the maximum number of rounds to apply as a delay to the message delivery, as an integer.
     */
    public RandomDelayMessageEngine(Topology topology, int maxDelay){
        super(topology, maxDelay);
        assert(maxDelay > 0);
    }

    @Override
    protected boolean noCachingNeeded(List<Message> newMessages) {
        return false;
    }

    @Override
    protected Map<Integer, List<Message>> prepareNewMessagesForCaching(List<Message> newMessages) {
        Map<Integer,List<Message>> messagesMap = new HashMap<>();

        for(Message msg: newMessages)
            cacheMessageAtTime(messagesMap, msg, getDeliveryDateForMessage(msg));

        return messagesMap;
    }

    /**
     * <p>Computes the delivery date (round number) for the provided {@link Message}.</p>
     * @param message the {@link Message} needing a delivery date.
     * @return the delivery date (round number) for the message.
     */
    protected int getDeliveryDateForMessage(Message message) {
        return currentTime - 1 + getDelayForMessage(message);
    }

    @Override
    protected int getDelayForMessage(Message message) {
        return r.nextInt(getDelay());
    }
}
