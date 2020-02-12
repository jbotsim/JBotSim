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

package examples.misc.randomwalks;

import io.jbotsim.core.Node;

import io.jbotsim.core.Color;
import java.util.Random;

/**
 * Created by acasteig on 17/06/15.
 */
public class RWNode1 extends Node {
    Random random = new Random();

    @Override
    public void onSelection() {
        setColor(Color.black);
    }

    @Override
    public void onClock() {
        if (getColor() == Color.black) {
            Node next = getNeighbors().get(random.nextInt(getNeighbors().size()));
            next.setColor(Color.black);
            setColor(null);
        }
    }
}
