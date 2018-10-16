package io.jbotsim.contrib.obstacle.shapes2d;

import io.jbotsim.core.Point;
import io.jbotsim.contrib.obstacle.ui.ObstacleDrawable;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;
import java.util.List;


public class LinesObstacleDrawable extends LinesObstacle implements ObstacleDrawable {


    public LinesObstacleDrawable(List<Point> points) {
        super(points);
    }

    public void paint(UIComponent uiComponent){
        Graphics g = (Graphics) uiComponent.getComponent();
        
        Color tmp=g.getColor();
        g.setColor(Color.blue);
        
        Point a= points.get(0);
        for (int i=1;i<points.size();++i){
            Point b=points.get(i);
            
            g.drawLine((int)a.getX(), (int)a.getY(),(int) b.getX(), (int)b.getY());
            a=b;
        }
        
        g.setColor(tmp);
    }

}
