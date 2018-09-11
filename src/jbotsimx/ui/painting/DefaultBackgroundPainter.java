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

import jbotsim.Node;
import jbotsim.Topology;

import java.awt.*;


public class DefaultBackgroundPainter implements BackgroundPainter {
    @Override
    public void paintBackground(Graphics2D g2d, Topology tp) {
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
