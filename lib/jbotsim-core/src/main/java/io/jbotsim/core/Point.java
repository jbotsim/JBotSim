/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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
package io.jbotsim.core;

import java.io.Serializable;

/**
 * <p>The {@link Point} used by JBotSim.</p>
 *
 * <p>The third dimension is optional. When not provided, it is implicitly set to zero.</p>
 */
public class Point implements Serializable {
    public double x;
    public double y;
    public double z;
    private int nbDimensions;

    /**
     * <p>Creates a {@link Point} object.</p>
     * <p>The coordinates correspond to the origin of the system.</p>
     */
    public Point() {
        this(0, 0);
    }

    /**
     * <p>Copy constructor.</p>
     * @param p the {@link Point} to copy.
     */
    public Point(Point p) {
        this(p.x, p.y, p.z);
        this.nbDimensions = p.nbDimensions;
    }

    /**
     * <p>Creates a {@link Point} object with the provided coordinates.</p>
     * @param x the abscissa, as a double.
     * @param y the ordinate, as a double.
     */
    public Point(double x, double y) {
        this(x, y, 0);
        nbDimensions = 2;
    }

    /**
     * <p>Creates a {@link Point} object with the provided coordinates.</p>
     * @param x the abscissa, as a double.
     * @param y the ordinate, as a double.
     * @param z the applicate, as a double.
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        nbDimensions = 3;
    }

    /**
     * <p>Computes the distance between two points.</p>
     * @param x1 abscissa of the first point, as a double.
     * @param y1 ordinate of the first point, as a double.
     * @param x2 abscissa of the second point, as a double.
     * @param y2 ordinate of the second point, as a double.
     * @return the distance between the two provided points, as a double.
     */
    public static double distance(double x1, double y1,
                                  double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    /**
     * <p>Computes the distance between the current point and the point represented by the provided coordinates.</p>
     * @param x the abscissa, as a double.
     * @param y the ordinate, as a double.
     * @param z the applicate, as a double.
     * @return the distance between the two provided points, as a double.
     */
    public double distance(double x, double y, double z) {
        double d1 = getX() - x;
        double d2 = getY() - y;
        double d3 = getZ() - z;
        return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
    }

    /**
     * <p>Gets the current applicate.</p>
     * @return the current applicate, as a double.
     */
    public double getZ() {
        return z;
    }

    /**
     * <p>Gets the current abscissa.</p>
     * @return the current abscissa, as a double.
     */
    public double getX() {
        return x;
    }

    /**
     * <p>Gets the current ordinate.</p>
     * @return the current ordinate, as a double.
     */
    public double getY() {
        return y;
    }

    /**
     * <p>Changes the current coordinates to those of the provided {@link Point}.</p>
     * @param p the {@link Point} to copy coordinates from.
     */
    public void setLocation(Point p) {
        setLocation(p.getX(), p.getY(), p.getZ());
    }

    /**
     * <p>Changes the current coordinates to those the provided ones.</p>
     * @param x the new abscissa, as a double.
     * @param y the new ordinate, as a double.
     */
    public void setLocation(double x, double y) {
        setLocation(x, y, 0);
    }

    /**
     * <p>Changes the current coordinates to those the provided ones.</p>
     * @param x the new abscissa, as a double.
     * @param y the new ordinate, as a double.
     * @param z the new applicate, as a double.
     */
    protected void setLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * <p>Computes the distance between the current point and the point represented by the provided coordinates.</p>
     * @param x the abscissa, as a double.
     * @param y the ordinate, as a double.
     * @return the distance between the two provided points, as a double.
     */
    public double distance(double x, double y) {
        x -= getX();
        y -= getY();
        return Math.sqrt(x * x + y * y);
    }

    /**
     * <p>Computes the distance between the current point and provided one.</p>
     * @param pt the other {@link Point}.
     * @return the distance between the two provided points, as a double.
     */
    public double distance(Point pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        double pz = pt.getZ() - this.getZ();
        return Math.sqrt(px * px + py * py + pz * pz);
    }

    @Override
    public Object clone() {
        return new Point(this.x, this.y, this.z);
    }

    @Override
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(getX());
        bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p2d = (Point) obj;
            return (getX() == p2d.getX()) && (getY() == p2d.getY()) && (getZ() == p2d.getZ());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        String s = "Point [x = " + getX() + ", y = " + getY();
        s += (nbDimensions == 3) ? ", z = " + getZ() + "]" : "]";
        return s;
    }
}
