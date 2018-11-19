package io.jbotsim.topology;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.List;

public class Layouts {
    public static final double DEFAULT_MARGIN = 10.0 / 100.0;

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
