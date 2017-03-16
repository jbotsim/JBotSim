package jbotsimx.obstacle.core;

import java.util.List;

public interface ObstacleListener{
    
    void onDetectedObstacles(List<Obstacle> obstacles);

}
