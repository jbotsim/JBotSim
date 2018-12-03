/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package io.jbotsim.ui;

/**
 * <p>The {@link CommandListener} is an element which is used to be called when a command arises.</p>
 */
public interface CommandListener {
    /**
     * Provides a way to react upon command selection.
     *
     * @param command the command which has risen.
     */
    void onCommand(String command);
}
