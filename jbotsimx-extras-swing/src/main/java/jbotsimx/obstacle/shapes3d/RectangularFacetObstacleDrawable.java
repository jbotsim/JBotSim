package jbotsimx.obstacle.shapes3d;

import jbotsim.Point;
import jbotsimx.obstacle.ui.ObstacleDrawable;
import jbotsimx.ui.painting.UIComponent;

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
