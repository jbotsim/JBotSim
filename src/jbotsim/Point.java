package jbotsim;

import java.io.Serializable;

public class Point {
    private double x;
    private double y;
    private double z = 0;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static double distanceSq(double x1, double y1,
                                    double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return (x1 * x1 + y1 * y1);
    }

    public static double distance(double x1, double y1,
                                  double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public double distance(double paramDouble1, double paramDouble2, double paramDouble3) {
        double d1 = getX() - paramDouble1;
        double d2 = getY() - paramDouble2;
        double d3 = getZ() - paramDouble3;
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
    }

    public double distanceSq(double px, double py) {
        px -= getX();
        py -= getY();
        return (px * px + py * py);
    }

    public double distanceSq(Point pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        double pz = pt.getZ() - this.getZ();
        return (px * px + py * py + pz * pz);
    }

    public double distance(double px, double py) {
        px -= getX();
        py -= getY();
        return Math.sqrt(px * px + py * py);
    }

    public double distance(Point pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        return Math.sqrt(px * px + py * py);
    }

    public Object clone() {
        return new Point(this.x, this.y);
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

    public static class Float extends Point implements Serializable {

        private static final long serialVersionUID = -2870572449815403710L;
        public float x;
        public float y;
        public float z = 0;


        public Float() {
        }


        public Float(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return (double) x;
        }

        public double getZ() {
            return (double) z;
        }

        public double getY() {
            return (double) y;
        }

        public void setLocation(double x, double y) {
            this.x = (float) x;
            this.y = (float) y;
        }

        public void setLocation(float x, float y) {
            this.x = x;
            this.y = y;
        }
        public void setLocation(double x, double y, double z) {
            this.x = (float) x;
            this.y = (float) y;
            this.z = (float) z;
        }

        public void setLocation(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return "Point [x = " + getX() + ", y = " + getY() + ", z = " + getZ() + "]";
        }
    }

    public static class Double extends Point implements Serializable {

        /*
         * JDK 1.6 serialVersionUID
         */
        private static final long serialVersionUID = 6150783262733311327L;
        public double x;
        public double y;
        public double z = 0;


        public Double() {
        }


        public Double(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public Double(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
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

        public void setLocation(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public void setLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return "Point [x = " + getX() + ", y = " + getY() + ", z = " + getZ() + "]";
        }
    }
}
