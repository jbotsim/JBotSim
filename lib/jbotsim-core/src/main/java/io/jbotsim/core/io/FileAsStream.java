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
package io.jbotsim.core.io;

import java.io.*;

/**
 * The interface defines methods to be implemented for file access.
 *
 * Specific implementation should be provided for each platform.
 */
public interface FileAsStream {


    /**
     * Returns an {@link InputStream} object corresponding to the provided file name
     * @param filename the name of the file to open
     * @return a {@link InputStream}
     * @throws IOException in case of error when opening associated file
     */
    InputStream getInputStreamForName(String filename) throws IOException;

    /**
     * Returns an {@link OutputStream} object corresponding to the provided file name
     * @param filename the name of the file to open
     * @return a {@link OutputStream}
     * @throws IOException in case of error when opening associated file
     */
    OutputStream getOutputStreamForName(String filename) throws IOException;

}
