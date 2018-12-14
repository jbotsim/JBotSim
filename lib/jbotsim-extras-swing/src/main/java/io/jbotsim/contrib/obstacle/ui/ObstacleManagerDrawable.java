package io.jbotsim.contrib.obstacle.ui;

import io.jbotsim.core.Topology;
import io.jbotsim.contrib.obstacle.core.ObstacleManager;
import io.jbotsim.ui.JTopology;

/**
 * Drawable version of the Main class of the plugin (ObstacleManager).
 * @author mbarjon
 *
 */

public class ObstacleManagerDrawable extends ObstacleManager {


    public static void init(Topology topology, JTopology jTopology) {
        init(topology);
        jTopology.addBackgroundPainter(new ObstaclePainter());
    }
    
}
