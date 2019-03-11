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
package io.jbotsim.gen.basic.generators;

import io.jbotsim.core.Topology;

/**
 * A {@link TopologyGenerator} is able to generate a specific structure in an existing {@link Topology} object
 * according to some specific characteristics.
 */
public interface TopologyGenerator {

    /**
     * Modifies the provided {@link Topology} according to the implementation.
     * @param topology the {@link Topology} to be modified by the generation
     */
    void generate(Topology topology);
}
