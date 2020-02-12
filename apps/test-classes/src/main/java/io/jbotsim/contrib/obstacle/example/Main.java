/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.jbotsim.contrib.obstacle.example;

import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.contrib.obstacle.ui.ObstacleManagerDrawable;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.contrib.obstacle.core.ObstacleManager;
import io.jbotsim.contrib.obstacle.shapes2d.CircleObstacleDrawable;
import io.jbotsim.contrib.obstacle.shapes2d.LinesObstacleDrawable;

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

        ObstacleManagerDrawable.init(tp, jtp);
//        ObstacleManager.init(tp);

        List<Point> points = new ArrayList<>();
        points.add(new Point(100, 100));
        points.add(new Point(200, 400));
        points.add(new Point(500, 500));
        points.add(new Point(600, 300));

        LinesObstacleDrawable lo = new LinesObstacleDrawable(points);
        ObstacleManager.addObstacle(lo, tp);

        CircleObstacleDrawable co = new CircleObstacleDrawable(new Point(400, 400), 10);
        ObstacleManager.addObstacle(co, tp);
        co = new CircleObstacleDrawable(new Point(200, 200), 20);
        ObstacleManager.addObstacle(co, tp);
        co = new CircleObstacleDrawable(new Point(600, 200), 80);
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
