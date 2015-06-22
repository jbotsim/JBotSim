package jbotsim.ui;

import java.awt.*;

/**
 * Created by acasteig on 6/9/15.
 */
public interface SurfacePainter {
    /**
     * Provides a way to draw things on the background.
     * @param g The background graphics.
     */
    void onPaint(Graphics g);
}
