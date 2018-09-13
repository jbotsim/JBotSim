package jbotsimx.obstacle.example;

import jbotsim.Point;
import jbotsim.Topology;
import jbotsimx.ui.JTopology;
import jbotsimx.ui.JViewer;
import jbotsimx.obstacle.core.ObstacleManager;
import jbotsimx.obstacle.shapes2d.CircleObstacle;
import jbotsimx.obstacle.shapes2d.LinesObstacle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acasteig on 16/03/17.
 */
public class Main {

    public static void main(String[] args) {
        Topology tp = new Topology(800,600);
        tp.setDefaultNodeModel(ObstacleNode.class);
        JTopology jtp = new JTopology(tp);

        ObstacleManager.init(tp, jtp);

        List<Point> points = new ArrayList<>();
        points.add(new Point(100, 100));
        points.add(new Point(200, 400));
        points.add(new Point(500, 500));
        points.add(new Point(600, 300));

        LinesObstacle lo = new LinesObstacle(points);
        ObstacleManager.addObstacle(lo, tp);

        CircleObstacle co = new CircleObstacle(new Point(400, 400), 10);
        ObstacleManager.addObstacle(co, tp);
        co = new CircleObstacle(new Point(200, 200), 20);
        ObstacleManager.addObstacle(co, tp);
        co = new CircleObstacle(new Point(600, 200), 80);
        ObstacleManager.addObstacle(co, tp);

        tp.pause();
        tp.setDefaultNodeModel(ObstacleNode.class);
        tp.addNode(400, 500);
        tp.addNode(200, 500);
        tp.addNode(400, 300);
        tp.resume();
        new JViewer(jtp);
    }

}
