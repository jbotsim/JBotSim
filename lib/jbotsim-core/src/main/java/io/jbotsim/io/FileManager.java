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
package io.jbotsim.io;

import java.io.*;

/**
 * The {@link FileManager} provides the following methods:
 * <ul>
 *     <li>{@link #read(String)}: reads the content of a file</li>
 *     <li>{@link #write(String, String)}: writes some data in a file</li>
 * </ul>
 */
public class FileManager implements FileAsStream {

    @Override
    public InputStream getInputStreamForName(String filename) throws IOException {
        return new FileInputStream(new File(filename));
    }

    @Override
    public OutputStream getOutputStreamForName(String filename) throws IOException {
        return new FileOutputStream(new File(filename));
    }


    /**
     * Saves the provided data to the given file.
     *
     * @param filename The path to the file
     * @param data The data to be written
     */
    public void write(String filename, String data) {

        try {
            OutputStream outputStream = getOutputStreamForName(filename);
            IOUtils.writeStringToStream(outputStream, data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: should be thrown !
        }
    }

    /**
     * Read the content of the file specified by the filename.
     *
     * @param filename The absolute path to the file
     * @return the content of the file, as a {@link String}. Can be null.
     */
    public String read(String filename) {

        try {
            InputStream file = getInputStreamForName(filename);
            return IOUtils.readInputStreamContentAsString(file);
        } catch (IOException e) {
            e.printStackTrace(); // TODO: should be thrown !
        }
        return null;
    }

}
