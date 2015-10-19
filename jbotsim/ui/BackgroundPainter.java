package jbotsim.ui;

import jbotsim.Topology;

import java.awt.*;

/**
 * Created by acasteig on 6/9/15.
 */
public interface BackgroundPainter {
    /**
     * Provides a way to draw things on the background.
     * @param g The background graphics.
     */
    void paintBackground(Graphics2D g2d, Topology tp);
}
