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

/**
 * This simple interface defines basic methods to get a {@link FileManager}.
 */
public interface FileManagerProvider {

    /**
     * Sets the new {@link FileManager} to use.
     *
     * @param fileManager the new {@link FileManager} to use
     */
    void setFileManager(FileManager fileManager);

    /**
     * Provides a {@link FileManager}.
     * @return a {@link FileManager}. May be null.
     */
    FileManager getFileManager();
}
