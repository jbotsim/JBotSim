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


/**
 * <p>The {@link JBackgroundPainter} is JBotSim's default {@link BackgroundPainter} for AWT.</p>
 *
 */
public class JBackgroundPainter implements BackgroundPainter {
    @Override
    public void paintBackground(UIComponent uiComponent, Topology tp) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
        setStroke(g2d, tp);
        setRenderingHints(g2d, tp);
        setColor(g2d, tp);

        for (Node n : tp.getNodes()) {
            drawSensingRange(g2d, n);
        }
    }

    protected void drawSensingRange(Graphics2D g2d, Node n) {
        double sR = n.getSensingRange();
        if (sR > 0) {
            g2d.setColor(Color.gray);
            g2d.drawOval((int) n.getX() - (int) sR, (int) n.getY() - (int) sR, 2 * (int) sR, 2 * (int) sR);
        }
    }


    /**
     * <p>Sets the proper {@link java.awt.Color} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Topology}.</p>
     * <p>You can override this method if you need to change the {@link JBackgroundPainter}'s default color management.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param topology the associated {@link Topology}.
     */
    protected void setColor(Graphics2D g2d, Topology topology) {
        g2d.setColor(Color.gray);
    }

    /**
     * <p>Sets the proper {@link Stroke} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Topology}.</p>
     * <p>You can override this method if you need to change the {@link JBackgroundPainter}'s default {@link Stroke}.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param topology the associated {@link Topology}.
     */
    protected void setStroke(Graphics2D g2d, Topology topology) {
        g2d.setStroke(new BasicStroke(1));
    }

    /**
     * <p>Sets the proper {@link RenderingHints} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Topology}.</p>
     * <p>You can override this method if you need to change the {@link JBackgroundPainter} rendering behavior.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param topology the associated {@link Topology}.
     */
    protected void setRenderingHints(Graphics2D g2d, Topology topology) {
        // default does nothing
    }
}
