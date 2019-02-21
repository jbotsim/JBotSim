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
package io.jbotsim.ui.painting;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.awt.*;


public class JBackgroundPainter implements BackgroundPainter {
    @Override
    public void paintBackground(UIComponent uiComponent, Topology tp) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
        g2d.setStroke(new BasicStroke(1));
        for (Node n : tp.getNodes()) {
            double sR = n.getSensingRange();
            if (sR > 0) {
                g2d.setColor(Color.gray);
                g2d.drawOval((int) n.getX() - (int) sR, (int) n.getY() - (int) sR, 2 * (int) sR, 2 * (int) sR);
            }
        }
    }
}
