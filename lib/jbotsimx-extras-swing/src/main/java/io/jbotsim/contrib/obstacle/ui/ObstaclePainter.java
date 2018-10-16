package io.jbotsim.contrib.obstacle.ui;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.contrib.obstacle.core.Obstacle;
import io.jbotsim.contrib.obstacle.core.ObstacleManager;
import io.jbotsim.ui.painting.UIComponent;


/**
    ObstaclePainter is a class used to represent the obstacles on the JViewer and it inherited from JTopology
    @author mbarjon
    
 */
public class ObstaclePainter implements BackgroundPainter {
    
    private static final long serialVersionUID = 561583583776975365L;

    /**
         Build the ObstaclePainter
     */
    public ObstaclePainter() {
    }

    /**
        The methods is automatically call by the JViewer and draw all the nodes and all the obstacles
     */

    @Override
    public void paintBackground(UIComponent uiComponent, Topology topology) {
        for(Obstacle o : ObstacleManager.getObstacles(topology)){
            if(o instanceof ObstacleDrawable) {
                ObstacleDrawable drawable = (ObstacleDrawable) o;
                drawable.paint(uiComponent);
            }
        }
    }
}
