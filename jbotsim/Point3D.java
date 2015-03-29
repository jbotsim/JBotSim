package jbotsim;

/**
 * Lifted from JavaFX (under GPL licence).
 * This class is already part of Java SE since JDK 7 (Oracle only)
 * But it is not in OpenJDK 7 (should be in 9).
 * It will eventually be removed from JBotSim...
 */
public class Point3D {
    private double x;
    private double y;
    private double z;
    private int hash = 0;

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public Point3D(double paramDouble1, double paramDouble2, double paramDouble3) {
        this.x = paramDouble1;
        this.y = paramDouble2;
        this.z = paramDouble3;
    }

    public double distance(double paramDouble1, double paramDouble2, double paramDouble3) {
        double d1 = getX() - paramDouble1;
        double d2 = getY() - paramDouble2;
        double d3 = getZ() - paramDouble3;
        return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
    }

    public double distance(Point3D paramPoint3D) {
        return distance(paramPoint3D.getX(), paramPoint3D.getY(), paramPoint3D.getZ());
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this) return true;
        if ((paramObject instanceof Point3D)) {
            Point3D localPoint3D = (Point3D) paramObject;
            return (getX() == localPoint3D.getX()) && (getY() == localPoint3D.getY()) && (getZ() == localPoint3D.getZ());
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l = 7L;
            l = 31L * l + Double.doubleToLongBits(getX());
            l = 31L * l + Double.doubleToLongBits(getY());
            l = 31L * l + Double.doubleToLongBits(getZ());
            this.hash = ((int) (l ^ l >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "Point3D [x = " + getX() + ", y = " + getY() + ", z = " + getZ() + "]";
    }
}
