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

package examples.basic.mobilebroadcast;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;



/**
 * Created by acasteig on 2/20/15.
 */
public class MobileBroadcastNode extends Node{

    boolean informed;

    @Override
    public void onStart() {
        setDirection(Math.PI * 2 * Math.random());
        informed = false;
        setColor(null); // optional (for restart only)
    }

    @Override
    public void onSelection() {
        informed = true;
        setColor(Color.red);
    }

    @Override
    public void onClock() {
        if (informed)
            sendAll(new Message());
        move();
        wrapLocation();
    }

    @Override
    public void onMessage(Message message) {
        informed = true;
        setColor(Color.red);
    }
}
