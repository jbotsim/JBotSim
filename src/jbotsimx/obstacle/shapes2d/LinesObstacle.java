package jbotsimx.obstacle.shapes2d;

import jbotsim.Node;
import jbotsim.Point;
import jbotsimx.obstacle.core.Obstacle;

import java.awt.*;
import java.util.List;


public class LinesObstacle implements Obstacle {

    private List<Point> points;
    private double x;
    private double y;
    
    private double xmin;
    private double ymin;
    

    public LinesObstacle(List<Point> points) {
        this.points = points;
    }

    private boolean hasCollisionDroiteSegment(Point a,Point b,Point o,Point p){

        double abX = b.getX() - a.getX();
        double abY = b.getY() - a.getY();
        double apX = p.getX() - a.getX();
        double apY = p.getY() - a.getY();
        double aoX = o.getX() - a.getX();
        double aoY = o.getY() - a.getY();

        return ((abX*apY-abY*apX)*(abX*aoY-abY*aoX) <0);
    }

    private boolean hasCollisionSegSeg(Point a,Point b,Point o,Point p){
        if(hasCollisionDroiteSegment(a, b, o, p))
            if(hasCollisionDroiteSegment(o, p, a, b))
                return true;
        return false;
    }

    private boolean hasCollisionAABB(Point a, Point b,Point o,Point p) {
        Point opMinX;
        Point opMaxX;
        Point opMinY;
        Point opMaxY;
        Point abMinX;
        Point abMaxX;
        Point abMinY;
        Point abMaxY;

        if(o.getX() < p.getX()){
            opMinX=o;
            opMaxX=p;
        }
        else{
            opMinX=p;
            opMaxX=o;
        }
        if(a.getX()<b.getX()){
            abMinX=a;
            abMaxX=b;
        }
        else{
            abMinX=b;
            abMaxX=a;
        }    
        if(a.getY()<b.getY()){
            abMinY=a;
            abMaxY=b;
        }    
        else{
            abMinY=b;
            abMaxY=a;
        }
        if(o.getY() < p.getY()){
            opMinY=o;
            opMaxY=p;
        }
        else{
            opMinY=p;
            opMaxY=o;
        }

        return !(opMinX.getX() >= abMaxX.getX() || opMaxX.getX() <= abMinX.getX() 
                || opMinY.getY() >= abMaxY.getY() || opMaxY.getY() <= abMinY.getY());
    }

    @Override
    public boolean obstructLink(Node node1, Node node2) {

        Point a=points.get(0);
        Point b;
        Point o=node1.getLocation();
        Point p=node2.getLocation();
        for(int i=1;i<points.size();i++){
            b=points.get(i);
            if(hasCollisionAABB(a,b,o,p))
                if(hasCollisionSegSeg(a, b, o, p))
                    return true;
            a=b;
        }

        return false;
    }
    
    public void paint(Graphics g){
        
        Color tmp=g.getColor();
        g.setColor(Color.blue);
        
        Point a=points.get(0);
        for (int i=1;i<points.size();++i){
            Point b=points.get(i);
            
            g.drawLine((int)a.getX(), (int)a.getY(),(int) b.getX(), (int)b.getY());
            a=b;
        }
        
        g.setColor(tmp);
    }

//    public List<Point> getPoints() {
//        return points;
//    }
    
    private double distanceMinSeg(Point v,Point w, Point p){
        double px=w.getX()-v.getX();
        double py=w.getY()-v.getY();
        double dist2=px*px +py*py;
        double t=((p.getX()-v.getX())*px +(p.getY()-v.getY())*py)/dist2;
        if (t<0)
            t=0;
        if(t>1)
            t=1;
        x=v.getX()+t*px;
        y=v.getY()+t*py;
        double dx=x-p.getX();
        double dy=y-p.getY();
        return Math.sqrt(dx*dx +dy*dy);
    }
    
//    @Override
//    public double minimumDistance(Node node) {
//
//        NodePoint n = new NodePoint(node.getX(),node.getY(),node.getZ());
//        return pointAtMinimumDistance(node).distance(n);
//    }

    @Override
    public Point pointAtMinimumDistance(Node node) {

        Point p =node.getLocation();
        Point a=points.get(0);
        Point b;
        double distance=Double.POSITIVE_INFINITY;
        for(int i=1;i<points.size();i++){
            b=points.get(i);
            double tmp=distanceMinSeg(a,b,p);
            if (tmp< distance){
                distance=tmp;
                ymin=y;
                xmin=x;
            }
            a=b;
        }
        return new Point(xmin, ymin, node.getZ());
    }
}
