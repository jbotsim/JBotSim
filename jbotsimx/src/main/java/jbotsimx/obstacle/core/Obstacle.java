package jbotsimx.obstacle.core;

import jbotsim.Node;
import jbotsim.Point;

import java.awt.*;

/**
 * Interface for all the obstacles (3D obstacles and 2D obstacles)
 * @author mbarjon
 *
 */
public interface Obstacle {

    /**
     * This function is automatically call by the obstacleDetector to known if the obstacle obstructs the link between node1 and node2
     * @param node1 First node of the link
     * @param node2    Second node of the link
     * @return true if the obstacle obstructs the link false if not
     */
    boolean obstructLink(Node node1,Node node2);
    
//    /**
//     * This function return the minimum distance between the obstacle and the given node
//     * @param node The node which you want to know is minimum distance from the obstacle
//     * @return The minimum distance between the obstacle and the node
//     */
//    public double minimumDistance(Node node);
    
    /**
     * This function return the Point on the obstacle which is at the minimum distance from the given node
     * @param node The node which you want to know the point on the obstacle which is at the minimum distance from it
     * @return The point on the obstacle which is at the minimum distance from the node
     */
    Point pointAtMinimumDistance(Node node);
    
    /**
     * This function is automatically call by ObstaclePainter to draw the obstacle
     * @param g The Graphics where the obstacle will be draw
     */
    void paint(Graphics g);
    
}
