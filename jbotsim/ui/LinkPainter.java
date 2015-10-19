package jbotsim.ui;

import jbotsim.Link;

import java.awt.*;

/**
 * Created by acasteig on 10/16/15.
 */
public interface LinkPainter {
    /**
     * Provides a way to add extra drawing to the Links.
     * @param g2d The graphics object.
     */
    void paintLink(Graphics2D g2d, Link link);
}
