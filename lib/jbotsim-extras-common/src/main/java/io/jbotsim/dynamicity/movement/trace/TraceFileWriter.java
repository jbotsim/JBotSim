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
package io.jbotsim.dynamicity.movement.trace;

/**
 * The {@link TraceFileWriter} is store a sequence of {@link TraceEvent}s and store them in a file.
 */
public interface TraceFileWriter {

    /**
     * Add a {@link TraceEvent} to the set of events.
     * @param e the event to add
     */
    void addTraceEvent(TraceEvent e);

    /**
     * Write the current trace content to the corresponding file
     * @param filename the name of the destination file
     * @throws Exception in case of error
     */
    void write(String filename) throws Exception;
}
