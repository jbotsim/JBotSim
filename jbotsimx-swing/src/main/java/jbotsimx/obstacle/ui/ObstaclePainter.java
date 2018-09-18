package jbotsimx.obstacle.ui;

import jbotsim.Topology;
import jbotsimx.ui.painting.BackgroundPainter;
import jbotsimx.obstacle.core.Obstacle;
import jbotsimx.obstacle.core.ObstacleManager;
import jbotsimx.ui.painting.UIComponent;

import java.awt.*;


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
