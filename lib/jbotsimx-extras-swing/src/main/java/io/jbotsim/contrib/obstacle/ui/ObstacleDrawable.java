package io.jbotsim.contrib.obstacle.ui;

import io.jbotsim.contrib.obstacle.core.Obstacle;
import io.jbotsim.ui.painting.UIComponent;

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
