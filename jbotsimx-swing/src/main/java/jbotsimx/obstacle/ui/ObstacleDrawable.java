package jbotsimx.obstacle.ui;

import jbotsimx.obstacle.core.Obstacle;
import jbotsimx.ui.painting.UIComponent;

import java.awt.*;

/**
 * Interface for drawable obstacles (3D obstacles and 2D obstacles)
 *
 */
public interface ObstacleDrawable extends Obstacle {

    /**
     * This function is automatically call by ObstaclePainter to draw the obstacle
     * @param uiComponent The UIComponent on which the obstacle will be drawn
     */
    void paint(UIComponent uiComponent);
    
}
