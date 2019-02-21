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

import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;

import java.awt.*;


public class JLinkPainter implements LinkPainter {
    @Override
    public void paintLink(UIComponent uiComponent, Link link) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
        Integer width = link.getWidth();
        if (width == 0)
            return;
        g2d.setColor(new java.awt.Color(link.getColor().getRGB()));
        g2d.setStroke(new BasicStroke(width));
        int srcX = (int) link.source.getX(), srcY = (int) link.source.getY();
        int destX = (int) link.destination.getX(), destY = (int) link.destination.getY();
        g2d.drawLine(srcX, srcY, (srcX + (destX - srcX)), (srcY + (destY - srcY)));
        Topology topology = link.source.getTopology();
        if (topology.hasDirectedLinks()) { // FIXME sometimes topology is null here
            int x = srcX + 4 * (destX - srcX) / 5 - 2;
            int y = srcY + 4 * (destY - srcY) / 5 - 2;
            g2d.drawOval(x, y, 4, 4);
        }
    }
}
