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

public class DefaultNodeScheduler implements NodeScheduler {

    @Override
    public void onClock(Topology tp) {
        for (Node node : tp.getNodes())
            node.onPreClock();
        for (Node node : tp.getNodes())
            node.onClock();
        for (Node node : tp.getNodes())
            node.onPostClock();
    }
}
