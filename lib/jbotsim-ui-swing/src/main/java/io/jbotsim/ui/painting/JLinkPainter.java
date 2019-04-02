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

/**
 * <p>The {@link JLinkPainter} is JBotSim's default {@link LinkPainter} for AWT.</p>
 *
 */
public class JLinkPainter implements LinkPainter {
    @Override
    public void paintLink(UIComponent uiComponent, Link link) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
        Integer width = link.getWidth();
        if (width == 0)
            return;

        setColor(g2d, link);
        setStroke(g2d, link);
        setRenderingHints(g2d, link);

        drawLink(g2d, link);
    }

    protected void drawLink(Graphics2D g2d, Link link) {
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


    /**
     * <p>Sets the proper {@link java.awt.Color} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Link}.</p>
     * <p>You can override this method if you need to change the {@link JLinkPainter}'s default color management.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param link the associated {@link Link}.
     */
    protected void setColor(Graphics2D g2d, Link link) {
        g2d.setColor(new Color(link.getColor().getRGB()));
    }

    /**
     * <p>Sets the proper {@link Stroke} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Link}.</p>
     * <p>You can override this method if you need to change the {@link JLinkPainter}'s default {@link Stroke}.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param link the associated {@link Link}.
     */
    protected void setStroke(Graphics2D g2d, Link link) {
        g2d.setStroke(new BasicStroke(link.getWidth()));
    }

    /**
     * <p>Sets the proper {@link RenderingHints} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Link}.</p>
     * <p>You can override this method if you need to change the {@link JLinkPainter} rendering behavior.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param link the associated {@link Link}.
     */
    protected void setRenderingHints(Graphics2D g2d, Link link) {
        // default does nothing
    }
}
