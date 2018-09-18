package jbotsimx.obstacle.shapes2d;

import jbotsim.Point;
import jbotsimx.obstacle.core.Obstacle;
import jbotsimx.obstacle.ui.ObstacleDrawable;
import jbotsimx.ui.painting.UIComponent;

import java.awt.*;


public class CircleObstacleDrawable extends CircleObstacle implements ObstacleDrawable {

    public CircleObstacleDrawable(Point center, double radius) {
        super(center, radius);
    }

    @Override
    public void paint(UIComponent uiComponent) {
        Graphics g = (Graphics) uiComponent.getComponent();
        Color c= g.getColor();
        g.setColor(Color.BLUE);
        g.drawOval((int) (center.getX() - radius), (int) (center.getY() - radius), (int) radius * 2, (int) radius * 2);
        g.setColor(c);
    }


}
