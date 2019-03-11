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

package io.jbotsim.gen.basic;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.List;

public class TopologyLayouts {
    public static final double DEFAULT_MARGIN = 10.0 / 100.0;


    /**
     * <p>Automatically scales the position of the {@link Node}s of the {@link Topology} to match its boundaries.</p>
     * <p>Note that this method changes the {@link Node}s' position without respect for their
     * {@link io.jbotsim.core.Link}s.</p>
     * @param topology the {@link Topology} to scale
     */
    private static void autoscale(Topology topology){
        double Xmax = 0, Ymax = 0, Xmin = Double.MAX_VALUE, Ymin = Double.MAX_VALUE;
        for (Node node : topology.getNodes()){
            if (node.getX() > Xmax)
                Xmax = node.getX();
            if (node.getY() > Ymax)
                Ymax = node.getY();
            if (node.getX() < Xmin)
                Xmin = node.getX();
            if (node.getY() < Ymin)
                Ymin = node.getY();
        }
        //FIXME: strange behaviour expected with single Node Topology (zero division)
        double width = Xmax - Xmin;
        double height = Ymax - Ymin;
        double availableWidth = topology.getWidth()*0.8;
        double availableHeight = topology.getHeight()*0.8;
        double scale = Math.min(availableWidth/width, availableHeight/height);
        for (Node node : topology.getNodes()){
            node.setLocation(node.getX() - Xmin, node.getY() - Ymin);
            node.setLocation(node.getX()*scale + topology.getWidth()*0.1, node.getY()*scale + topology.getHeight()*0.1);
        }
    }

    public static void circle(Topology tp, double margin) {
        List<Node> nodes = tp.getNodes();
        if (nodes.isEmpty())
            return;

        double w = tp.getWidth();
        double h = tp.getHeight();
        double xc = w / 2.0;
        double yc = h / 2.0;
        double radius = ((w < h ? w : h) * (1.0 - margin)) / 2.0;

        double angle = 0.0;
        double angleinc = 2.0 * Math.PI / nodes.size();
        for (Node n : nodes) {
            n.setLocation(xc + radius * Math.cos(angle), yc + radius * Math.sin(angle));
            angle += angleinc;
        }
    }

    public static void circle(Topology tp) {
        circle(tp, DEFAULT_MARGIN);
    }

    public static void ellipse(Topology tp, double xmargin, double ymargin) {
        List<Node> nodes = tp.getNodes();
        if (nodes.isEmpty())
            return;

        double w = tp.getWidth();
        double h = tp.getHeight();
        double xc = w / 2.0;
        double yc = h / 2.0;
        double xradius = w * (1.0 - xmargin) / 2.0;
        double yradius = h * (1.0 - ymargin) / 2.0;

        double angle = 0.0;
        double angleinc = 2.0 * Math.PI / nodes.size();
        for (Node n : nodes) {
            n.setLocation(xc + xradius * Math.cos(angle), yc + yradius * Math.sin(angle));
            angle += angleinc;
        }
    }

    public static void ellipse(Topology tp) {
        ellipse(tp, DEFAULT_MARGIN, DEFAULT_MARGIN);
    }

    public static void line(Topology tp, double margin) {
        List<Node> nodes = tp.getNodes();
        if (nodes.isEmpty())
            return;

        double w = tp.getWidth();
        double h = tp.getHeight();
        double x, y, dx, dy;

        if (w < h) {
            // vertical line
            x = w / 2.0;
            y = h * margin;
            dx = dy = 0.0;
            if (nodes.size() > 1)
                dy = h * (1.0 - 2.0 * margin) / (nodes.size() - 1);
        } else {
            // horizontal line
            x = w * margin;
            y = h / 2.0;
            dx = dy = 0.0;
            if (nodes.size() > 1)
                dx = w * (1.0 - 2.0 * margin) / (nodes.size() - 1);
        }

        for (Node n : nodes) {
            n.setLocation(x, y);
            x += dx;
            y += dy;
        }
    }

    public static void line(Topology tp) {
        line(tp, DEFAULT_MARGIN);
    }
}
