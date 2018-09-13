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
package jbotsim;


public class LinkResolver {
    public boolean isHeardBy(Node n1, Node n2) {
        return (n1.isWirelessEnabled() && n2.isWirelessEnabled()
                && n1.distance(n2) < n1.getCommunicationRange());
    }
}
