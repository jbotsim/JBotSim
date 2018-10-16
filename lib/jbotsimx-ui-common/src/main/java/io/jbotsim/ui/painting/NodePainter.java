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
package io.jbotsim.ui.painting;

import io.jbotsim.core.Node;

public interface NodePainter {
    /**
     * Provides a way to redefine the drawing of a node.
     *
     * @param g2d  This node graphics object
     * @param node This node
     */
    void paintNode(UIComponent g2d, Node node);
}
