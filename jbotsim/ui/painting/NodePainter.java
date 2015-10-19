package jbotsim.ui.painting;

import jbotsim.Node;
import jbotsim.Topology;

import java.awt.*;

/**
 * Created by acasteig on 6/9/15.
 */
public interface NodePainter {
    /**
     * Provides a way to redefine the drawing of a node.
     * @param g2d This node graphics object
     * @param node This node
     */
    void paintNode(Graphics2D g2d, Node node);
}
