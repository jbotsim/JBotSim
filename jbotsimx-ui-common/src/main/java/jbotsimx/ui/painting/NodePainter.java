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

public interface NodePainter {
    /**
     * Provides a way to redefine the drawing of a node.
     *
     * @param g2d  This node graphics object
     * @param node This node
     */
    void paintNode(Graphics2D g2d, Node node);
}
