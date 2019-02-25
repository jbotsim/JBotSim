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
package io.jbotsim.gen.dynamic.trace;

/**
 * The {@link TraceFileReader} is able to parse a file content and put it into a {@link TracePlayer}.
 */
public interface TraceFileReader {

    /**
     * Parse the content of the file named filename and put it in the provided {@link TracePlayer}.
     * @param filename the filename of the source file
     * @param tracePlayer the {@link TracePlayer} which must be populated
     * @throws Exception in case of error
     */
    void parse(String filename, TracePlayer tracePlayer) throws Exception;
}
