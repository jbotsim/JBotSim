package jbotsimx.ui.painting;

import jbotsim.Link;

import java.awt.*;

public interface LinkPainter {
    /**
     * Paints the Links.
     *
     * @param g2d  The graphics object
     * @param link The link to be drawn
     */
    void paintLink(Graphics2D g2d, Link link);
}
