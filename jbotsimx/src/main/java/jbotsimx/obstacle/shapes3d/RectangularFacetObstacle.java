package jbotsimx.obstacle.shapes3d;

import jbotsim.Node;
import jbotsim.Point;
import jbotsimx.obstacle.core.Obstacle;

import java.awt.*;

public class RectangularFacetObstacle implements Obstacle {

    private Point a;
    private Point b;
    private Point c;

    private Vector3D ab;
    private Vector3D ac;
    private Vector3D n;

    public RectangularFacetObstacle(Point a, Point b, Point c)
            throws NotPerpendicularException {
        this.a = a;
        this.b = b;
        this.c = c;

        ab = new Vector3D(a, b);
        ac = new Vector3D(a, c);

        if (ab.dot(ac) != 0)
            throw new NotPerpendicularException();

        Vector3D bc = new Vector3D(b, c);

        n = ab.produitVectorielWith(bc);

        n = n.dividedBy(n.norme());
    }

    @Override
    public boolean obstructLink(Node node1, Node node2) {

        Point n1 = new Point(node1.getX(), node1.getY(), node1.getZ());
        Point n2 = new Point(node2.getX(), node2.getY(), node2.getZ());

        Vector3D u = new Vector3D(n1, n2);
        Vector3D w = new Vector3D(a, n1);

        double d = n.dot(u);
        double num = -n.dot(w);

        if (Math.abs(d) < 0.000001)
            return num == 0 && checkIfInsideSurface(projectOnVertor(ab, n1), projectOnVertor(ac, n1));
        double sI = num / d;
        if (sI < 0 || sI > 1)
            return false;

        Point n = new Point(n1.getX() + u.vx * sI, n1.getY() + u.vy
                * sI, n1.getZ() + u.vz * sI);

        return checkIfInsideSurface(projectOnVertor(ab, n),
                projectOnVertor(ac, n));
    }

    private double projectOnVertor(Vector3D v, Point n) {
        Vector3D an = new Vector3D(a, n);
        return an.dot(v) / v.norme();
    }

    private boolean checkIfInsideSurface(double distanceNAB, double distanceNAC) {

        if (distanceNAB < 0 || distanceNAB > a.distance(b))
            return false;
        if (distanceNAC < 0 || distanceNAC > a.distance(c))
            return false;

        return true;
    }

//    @Override
//    public double minimumDistance(Node node) {
//        NodePoint n = new NodePoint(node.getX(), node.getY(), node.getZ());
//        return pointAtMinimumDistance(node).distance(n);
//    }

    @Override
    public Point pointAtMinimumDistance(Node node) {

        Point n = new Point(node.getX(), node.getY(), node.getZ());

        double distanceNAB = projectOnVertor(ab, n);
        double distanceNAC = projectOnVertor(ac, n);

        Vector3D unitaryAB = new Vector3D(ab);
        unitaryAB.dividedBy(ab.norme());

        Vector3D unitaryAC = new Vector3D(ac);
        unitaryAC.dividedBy(ac.norme());

        Vector3D NAB = unitaryAB.multipliedBy(distanceNAB);

        Vector3D NAC = unitaryAC.multipliedBy(distanceNAC);

        double distanceAB = a.distance(b);
        double distanceAC = a.distance(c);

        if (distanceNAB < 0) {
            if (distanceNAC < 0) {
                return a;
            } else if (distanceNAC > distanceAC) {
                return c;
            } else {
                return NAC.getNewPointFrom(a);
            }
        } else if (distanceNAB > distanceAB) {
            if (distanceNAC < 0) {
                return b;
            } else if (distanceNAC > distanceAC) {
                return ac.getNewPointFrom(b);
            } else {
                return NAC.getNewPointFrom(b);
            }
        } else {
            if (distanceNAC < 0) {
                return NAB.getNewPointFrom(a);
            } else if (distanceNAC > distanceAC) {
                return NAB.getNewPointFrom(c);
            } else {
                return NAB.sum(NAC).getNewPointFrom(a);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        // not implemented for the moment
    }

}
