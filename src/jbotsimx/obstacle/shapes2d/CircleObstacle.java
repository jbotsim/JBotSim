package jbotsimx.obstacle.shapes2d;

import jbotsim.Node;
import jbotsim.Point;
import jbotsim.Point;
import jbotsimx.obstacle.core.Obstacle;

import java.awt.*;


public class CircleObstacle implements Obstacle {

    private Point center;
    private double radius;
    
    public CircleObstacle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    private boolean pointInCircle(Point p){
        double dx=p.getX()-center.getX();
        double dy=p.getY()-center.getY();
        double d2=dx*dx+dy*dy;
        return (d2 <=radius*radius);
    }
    
    private boolean collisionDroite(Point a, Point b){
        Point u=new Point(b.getX()-a.getX(), b.getY()-a.getY());
        Point ac=new Point(center.getX()-a.getX(), center.getY()-a.getY());
        double num=u.getX()*ac.getY()-u.getY()*ac.getX();
        if (num<0)
            num=-num;
        double deno=Math.sqrt(u.getX()*u.getX()+u.getY()*u.getY());
        double ci=num/deno;
        return (ci<radius);
    }
    
    @Override
    public boolean obstructLink(Node node1, Node node2) {

        Point a=node1.getLocation();
        Point b=node2.getLocation();
        if(a.distance(center) <= radius && b.distance(center) <= radius )
            return false;
        if(collisionDroite(a, b)){
            Point ab=new Point(b.getX()-a.getX(), b.getY()-a.getY());
            Point ac=new Point(center.getX()-a.getX(), center.getY()-a.getY());
            Point bc=new Point(center.getX()-b.getX(), center.getY()-b.getY());
            
            double pscal1=ab.getX()*ac.getX()+ab.getY()*ac.getY();
            double pscal2=-ab.getX()*bc.getX() - ab.getY()*bc.getY();
            if(pscal1>=0 &&pscal2>=0 ||pointInCircle(a)||pointInCircle(b))
                return true;
        }
        
        return false;
    }

//    @Override
//    public double minimumDistance(Node node) {
//
//        NodePoint n = new NodePoint(node.getX(),node.getY(),node.getZ());
//        return pointAtMinimumDistance(node).distance(n);
//
//    }

    @Override
    public void paint(Graphics g) {

        Color c= g.getColor();
        g.setColor(Color.BLUE);
        g.drawOval((int) (center.getX() - radius), (int) (center.getY() - radius), (int) radius * 2, (int) radius * 2);
        g.setColor(c);
    }

    @Override
    public Point pointAtMinimumDistance(Node node) {

        Point p = node.getLocation();
        double angle = Math.atan2(p.getX()-center.getX(), -(p.getY()-center.getY()))-Math.PI/2;
        
        return new Point(center.getX()+Math.cos(angle)*radius,center.getY()+Math.sin(angle)*radius, node.getZ());
    }

}
