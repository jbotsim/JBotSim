package io.jbotsim.contrib.obstacle.shapes3d;

import io.jbotsim.Point;
import io.jbotsim.contrib.obstacle.ui.ObstacleDrawable;
import io.jbotsim.ui.painting.UIComponent;

public class RectangularFacetObstacleDrawable extends RectangularFacetObstacle implements ObstacleDrawable {

    public RectangularFacetObstacleDrawable(Point a, Point b, Point c)
            throws NotPerpendicularException {
        super(a, b, c);
    }

    @Override
    public void paint(UIComponent uiComponent) {
        // not implemented for the moment
    }

}
