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
package jbotsimx.ui.painting;

import jbotsim.Link;
import jbotsim.Topology;

import java.awt.*;


public class LinkPainter {
    /**
     * Paints the Links.
     *
     * @param g2d  The graphics object
     * @param link The link to be drawn
     */
    public void paintLink(Graphics2D g2d, Link link) {
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
