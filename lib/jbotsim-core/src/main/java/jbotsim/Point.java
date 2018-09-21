package jbotsim;

import java.io.Serializable;

public class Point implements Serializable {
    public double x;
    public double y;
    public double z;
    private int nbDimensions;

    public Point() {
        this(0, 0);
    }

    public Point(Point p) {
        this(p.x, p.y, p.z);
        this.nbDimensions = p.nbDimensions;
    }

    public Point(double x, double y) {
        this(x, y, 0);
        nbDimensions = 2;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        nbDimensions = 3;
    }

    public static double distance(double x1, double y1,
                                  double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public double distance(double x, double y, double z) {
        double d1 = getX() - x;
        double d2 = getY() - y;
        double d3 = getZ() - z;
        return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
    }

    public double getZ() {
        return z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setLocation(Point p) {
        setLocation(p.getX(), p.getY());
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public double distance(double px, double py) {
        px -= getX();
        py -= getY();
        return Math.sqrt(px * px + py * py);
    }

    public double distance(Point pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        double pz = pt.getZ() - this.getZ();
        return Math.sqrt(px * px + py * py + pz * pz);
    }

    public Object clone() {
        return new Point(this.x, this.y, this.z);
    }

    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(getX());
        bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p2d = (Point) obj;
            return (getX() == p2d.getX()) && (getY() == p2d.getY()) && (getZ() == p2d.getZ());
        }
        return super.equals(obj);
    }

    public String toString() {
        String s = "Point [x = " + getX() + ", y = " + getY();
        s += (nbDimensions == 3) ? ", z = " + getZ() + "]" : "]";
        return s;
    }
}
