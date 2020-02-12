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

package examples.fancy.vectorracer;

import io.jbotsim.core.Node;

import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class Drone extends VectorNode {
    Point nextCherry;

    @Override
    public void onStart() {
        super.onStart();
        setSensingRange(20);
        setIcon(Icons.DRONE);
        setIconSize(14);
        onPointReached(getLocation());
    }

    @Override
    public void onPointReached(Point point) {
        if (nextCherry == null)
            if (getTopology().getNodes().size() > 1)
                nextCherry = getTopology().getNodes().get(0).getLocation();
            else
                nextCherry = new Point(500, 400);
        travelTo(nextCherry);
    }

    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Cherry) {
            getTopology().removeNode(node);
            nextCherry = null;
        }
    }
}
