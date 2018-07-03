package jbotsimx.obstacle.example;

import jbotsim.Node;
import jbotsim.Point2D;
import jbotsim.Point3D;
import jbotsimx.obstacle.core.Obstacle;
import jbotsimx.obstacle.core.ObstacleListener;
import jbotsimx.obstacle.core.ObstacleManager;
import java.util.List;
import java.util.Random;

/**
 * Created by acasteig on 16/03/17.
 */
public class ObstacleNode extends Node implements ObstacleListener{

    private static Random r = new Random();

    public ObstacleNode() {
        setProperty("target", new Point2D(r.nextInt(800), r.nextInt(600)));
        setSensingRange(25);
    }

    @Override
    public void onStart() {
        setIcon("/jbotsimx/obstacle/example/cleaner.png");
        ObstacleManager.addObstacleListener(this, getTopology());
    }

    @Override
    public void onStop() {
        ObstacleManager.removeObstacleListener(this, getTopology());
    }

    @Override
    public void onDetectedObstacles(List<Obstacle> obstacles) {
        Point3D p = obstacles.get(0).pointAtMinimumDistance(this);
        Point3D tmp = new Point3D(this.getX()+Math.cos(this.getDirection()), this.getY() + Math.sin(this.getDirection()),0);
        double distance = p.distance(tmp);

        for (Obstacle o : obstacles){
            Point3D ptmp = o.pointAtMinimumDistance(this);
            if (ptmp.distance(tmp) < distance){
                p = ptmp;
                distance = p.distance(tmp);
            }
        }
        Point3D tmp2;
        Point3D n = new Point3D(this.getX(),this.getY(),this.getZ());
        if(p.distance(tmp) < this.getSensingRange() && p.distance(tmp) < p.distance(n)){
            do {
                setProperty("target", new Point2D(r.nextInt(800), r.nextInt(600)));
                Point2D target = (Point2D)getProperty("target");
                double direction = Math.atan2(target.getX() - this.getX(), - (target.getY() - this.getY())) - Math.PI/2;
                tmp2 = new Point3D(this.getX() + Math.cos(direction), this.getY() + Math.sin(direction),0);
            }
            while(p.distance(tmp2) < p.distance(n));
        }
    }

    @Override
    public void onClock() {
        Point2D target = (Point2D)getProperty("target");
        setDirection(target);
        move();
        if (distance(target) < 15)
            setProperty("target", new Point2D(r.nextInt(800), r.nextInt(600)));
    }

}
