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
import io.jbotsim.ui.JNode;

import java.awt.*;

/**
 * <p>The {@link JNodePainter} is JBotSim's default {@link NodePainter} for AWT.</p>
 *
 */
public class JNodePainter implements NodePainter {
    @Override
    public void paintNode(UIComponent uiComponent, Node node) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();

        if(node.getColor() == null)
            return;

        setStroke(g2d, node);
        setRenderingHints(g2d, node);
        setColor(g2d, node);

        drawNode(g2d, node);
    }

    protected void drawNode(Graphics2D g2d, Node node) {
        JNode jn = (JNode) node.getProperty("jnode");
        int drawSize = jn.getWidth() / 2;
        g2d.fillOval(drawSize / 2, drawSize / 2, drawSize, drawSize);
    }

    /**
     * <p>Sets the proper {@link java.awt.Color} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Node}.</p>
     * <p>You can override this method if you need to change the {@link JNodePainter}'s default color management.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param node the associated {@link Node}.
     */
    protected void setColor(Graphics2D g2d, Node node) {
        g2d.setColor(new Color(node.getColor().getRGB()));
    }

    /**
     * <p>Sets the proper {@link Stroke} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Node}.</p>
     * <p>You can override this method if you need to change the {@link JNodePainter}'s default {@link Stroke}.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param node the associated {@link Node}.
     */
    protected void setStroke(Graphics2D g2d, Node node) {
        // default does nothing
    }

    /**
     * <p>Sets the proper {@link RenderingHints} on the provided {@link Graphics2D} object, with respect to the provided
     * {@link Node}.</p>
     * <p>You can override this method if you need to change the {@link JNodePainter} rendering behavior.</p>
     * @param g2d a {@link Graphics2D} object.
     * @param node the associated {@link Node}.
     */
    protected void setRenderingHints(Graphics2D g2d, Node node) {
        // default does nothing
    }
}
