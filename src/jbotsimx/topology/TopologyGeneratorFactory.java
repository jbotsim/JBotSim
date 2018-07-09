package jbotsimx.topology;

import com.sun.tools.internal.xjc.reader.Ring;
import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopologyGeneratorFactory {
    private boolean directed = false;
    private boolean wired = false;
    private double x = 0;
    private double y = 0;
    private double width = 1.0;
    private double height = 1.0;
    private Class<? extends Node> nodeClass = Node.class;
    private int order = 1;
    private boolean absoluteCoords = false;

    public static void main(String[] args) {
        Topology tp = new Topology();

        TopologyGenerator.generateTorus(tp, 5);

        new JViewer(tp);
    }

    public boolean isAbsoluteCoords() {
        return absoluteCoords;
    }

    public void setAbsoluteCoords(boolean absoluteCoords) {
        this.absoluteCoords = absoluteCoords;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public boolean isWired() {
        return wired;
    }

    public void setWired(boolean wired) {
        this.wired = wired;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getAbsoluteX(Topology tp) {
        if (isAbsoluteCoords())
            return x;
        return x * tp.getWidth();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAbsoluteY(Topology tp) {
        if (isAbsoluteCoords())
            return y;
        return y * tp.getHeight();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getAbsoluteWidth(Topology tp) {
        if (isAbsoluteCoords())
            return width;
        return width * tp.getWidth();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getAbsoluteHeight(Topology tp) {
        if (isAbsoluteCoords())
            return height;
        return height * tp.getHeight();
    }

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass == null ? Node.class : nodeClass;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Generator newHorizontalLine() {
        return newLine(true);
    }

    public Generator newVerticalLine() {
        return newLine(false);
    }

    public Generator newLine(boolean horizontal) {
        return new LineGenerator(horizontal);
    }

    public Generator newRing() {
        return new RingGenerator();
    }

    public Generator newSquareGrid() {
        return newGrid(order, order);
    }

    public Generator newGrid(int xOrder, int yOrder) {
        return new GridGenerator(xOrder, yOrder);
    }

    public Generator newKN() {
        return new KNGenerator();
    }

    public Generator newTorus() {
        return new TorusGenerator(order, order);
    }

    public Generator newTorus(int xOrder, int yOrder) {
        return new TorusGenerator(xOrder, yOrder);
    }

    public static interface Generator {
        void generate(Topology tp);
    }

    private class LineGenerator implements Generator {
        private boolean horizontal;

        LineGenerator(boolean horizontal) {
            this.horizontal = horizontal;
        }

        @Override
        public void generate(Topology tp) {
            double w = getAbsoluteWidth(tp);
            double h = getAbsoluteHeight(tp);
            double x0 = getAbsoluteX(tp);
            double y0 = getAbsoluteY(tp);
            double dx = 0;
            double dy = 0;
            double cr;

            if (horizontal) {
                dx = w / (order + ((order > 1) ? -1 : 0));
                cr = dx + 1;
            } else {
                dy = h / (order + ((order > 1) ? -1 : 0));
                cr = dy + 1;
            }

            try {
                if (wired) {
                    Node pred = null;
                    Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
                    for (int i = 0; i < order; i++) {
                        Node n = nodeClass.getConstructor().newInstance();
                        n.setCommunicationRange(0.0);
                        n.setLocation(x0, y0);
                        n.disableWireless();
                        tp.addNode(n);
                        x0 += dx;
                        y0 += dy;

                        if (pred != null)
                            tp.addLink(new Link(pred, n, type, Link.Mode.WIRED));
                        pred = n;
                    }
                } else {
                    for (int i = 0; i < order; i++) {
                        Node n = nodeClass.getConstructor().newInstance();
                        n.setLocation(x0, y0);
                        n.setCommunicationRange(cr);
                        n.enableWireless();
                        tp.addNode(n);
                        x0 += dx;
                        y0 += dy;
                    }
                }
            } catch (ReflectiveOperationException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private class RingGenerator implements Generator {
        @Override
        public void generate(Topology tp) {
            try {
                Node[] nodes = generateNodes(tp);

                if (wired) {
                    Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
                    for (int i = 1; i < order; i++)
                        tp.addLink(new Link(nodes[i - 1], nodes[i], type));
                    tp.addLink(new Link(nodes[order - 1], nodes[0], type));
                } else {
                    for (int i = 0; i < order; i++) {
                        Node pred = (i == 0) ? nodes[order - 1] : nodes[i - 1];
                        Node next = (i == order - 1) ? nodes[0] : nodes[i + 1];
                        Node n = nodes[i];
                        n.enableWireless();
                        n.setCommunicationRange(1+Math.min(n.distance(pred), n.distance(next)));
                    }
                }
            } catch (ReflectiveOperationException e) {
                System.err.println(e.getMessage());
            }
        }

        protected Node[] generateNodes(Topology tp) throws ReflectiveOperationException {
            Node[] nodes = new Node[order];
            double arc = Math.PI * 2.0 / order;
            double x0 = getAbsoluteX(tp);
            double y0 = getAbsoluteY(tp);
            double xrad = getAbsoluteWidth(tp) / 2.0;
            double yrad = getAbsoluteHeight(tp) / 2.0;

            double angle = 0.0;
            for (int i = 0; i < order; i++) {
                Node n = nodeClass.getConstructor().newInstance();
                n.setLocation(x0 + xrad * Math.cos(angle), x0 + yrad * Math.sin(angle));
                tp.addNode(n);
                angle += arc;
                nodes[i] = n;
                if (wired) {
                    n.setCommunicationRange(0.0);
                    n.disableWireless();
                }
            }

            return nodes;
        }
    }

    private class KNGenerator extends RingGenerator {
        @Override
        public void generate(Topology tp) {
            try {
                Node[] nodes = generateNodes(tp);

                if (wired) {
                    for (int i = 0; i < order; i++)
                        for (int j = i + 1; j < order; j++)
                            tp.addLink(new Link(nodes[i], nodes[j], Link.Type.UNDIRECTED));
                } else {
                    for (Node n : nodes) {
                        n.enableWireless();
                        n.setCommunicationRange(Math.max(getAbsoluteHeight(tp), getAbsoluteWidth(tp)));
                    }
                }
            } catch (ReflectiveOperationException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private class GridGenerator implements Generator {
        protected int xOrder;
        protected int yOrder;
        protected double cr;

        GridGenerator(int xOrder, int yOrder) {
            this.xOrder = xOrder;
            this.yOrder = yOrder;
            this.cr = 0.0;
        }

        @Override
        public void generate(Topology tp) {
            try {
                generateGrid(tp);
            } catch (ReflectiveOperationException e) {
                System.err.println(e.getMessage());
            }
        }

        protected Node[][] generateGrid(Topology tp) throws ReflectiveOperationException {
            Node[][] nodes = generateNodes(tp);
            Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
            for (int i = 0; i < xOrder; i++) {
                for (int j = 0; j < yOrder; j++) {
                    Node n = nodes[i][j];
                    if (wired) {
                        n.disableWireless();
                        n.setCommunicationRange(0.0);

                        if (i < xOrder - 1) {
                            Link l = new Link(n, nodes[i + 1][j], type);
                            tp.addLink(l);
                        }
                        if (j < yOrder - 1) {
                            Link l = new Link(n, nodes[i][j + 1], type);
                            tp.addLink(l);
                        }
                    } else {
                        n.enableWireless();
                        n.setCommunicationRange(cr);
                    }
                }
            }
            return nodes;
        }

        private Node[][] generateNodes(Topology tp) throws ReflectiveOperationException {
            Node[][] result = new Node[xOrder][];
            double x0 = getAbsoluteX(tp);
            double y0 = getAbsoluteY(tp);
            double xStep = getAbsoluteWidth(tp) / (xOrder>1?xOrder-1:xOrder);
            double yStep = getAbsoluteHeight(tp) / (yOrder>1?yOrder-1:yOrder);
            cr = Math.max(xStep, yStep) + 1;

            for (int i = 0; i < xOrder; i++) {
                result[i] = new Node[yOrder];
                for (int j = 0; j < yOrder; j++) {
                    Node n = nodeClass.getConstructor().newInstance();
                    n.setLocation(x0 + i * xStep, y0 + j * yStep);
                    tp.addNode(n);
                    result[i][j] = n;
                }
            }

            return result;
        }
    }

    private class TorusGenerator extends GridGenerator {
        TorusGenerator(int xOrder, int yOrder) {
            super(xOrder, yOrder);
        }

        @Override
        public void generate(Topology tp) {
            try {
                Node[][] nodes = generateGrid(tp);

                for (int i = 0; i < xOrder; i++)
                    tp.addLink(new Link(nodes[i][0], nodes[i][yOrder - 1]));

                for (int j = 0; j < yOrder; j++)
                    tp.addLink(new Link(nodes[0][j], nodes[xOrder - 1][j]));

            } catch (ReflectiveOperationException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
