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

package io.jbotsim.ui.painting;


import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * <p>The {@link JParentLinkPainter} draws links in an oriented way
 * based on the value of a "parent" variable (if any exists).
 * The link is drawn from a node to its parent with arrow-tips at the parent side.
 * Further customization can be done similarly to a {@link JDirectedLinkPainter},
 * from which the behavior is inherited.</p>
 *
 * To use this painter, create a {@link JTopology} object
 * from your {@link Topology}, then add a {@link JParentLinkPainter} to it,
 * and finally create the {@link JViewer} from this {@link JTopology},
 * as follows:
 * <pre>
 * {@code
 * Topology topology = new Topology();
 * JTopology jTopology = new JTopology(topology);
 * jTopology.addLinkPainter(new JParentLinkPainter());
 * new JViewer(jTopology); // the argument is jTopology here, not topology.
 * }
 * </pre>
 */
public class JParentLinkPainter extends JDirectedLinkPainter {

    protected Node getParent(Node node) {
        try {
            Field field = node.getClass().getField("parent");
            return (Node) field.get(node);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        return null;
    }

    @Override
    protected void drawDestinationPartIfNeeded(Graphics2D g2d, Link link) {
        Node srcNode = link.source;
        Node destNode = link.destination;
        if (getParent(srcNode) == destNode)
            paintHead(g2d, srcNode, destNode);
        if (getParent(destNode) == srcNode)
            paintHead(g2d, destNode, srcNode);
    }

    protected void paintHead(Graphics2D g2d, Node srcNode, Node destNode){
        Point srcPoint = srcNode.getLocation();
        Point destPoint = destNode.getLocation();
        double destinationIconSize = destNode.getIconSize() ;
        printDirectLinkEnd(g2d, srcPoint, destPoint, destinationIconSize);
    }
}
