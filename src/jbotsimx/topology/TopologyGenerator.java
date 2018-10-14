package jbotsimx.topology;

import jbotsim.Topology;

public class TopologyGenerator {
    public static void generateLine(Topology tp, int order){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(100);
        gf.setWidth(tp.getWidth());
        gf.setHeight(tp.getHeight());
        gf.setWired(false);
        gf.setNodeClass(tp.getNodeModel("default"));
        gf.setOrder (order);
        gf.newHorizontalLine().generate(tp);
    }
    public static void generateRing(Topology topology, int nbNodes) {
        generateRing(topology, nbNodes, false);
    }
    public static void generateRing(Topology topology, int nbNodes, boolean directed){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(150);
        gf.setY(150);
        gf.setWidth(200);
        gf.setHeight(200.0);
        gf.setDirected(directed);
        gf.setWired(true);
        gf.setNodeClass(topology.getNodeModel("default"));
        gf.setOrder (nbNodes);
        gf.newRing().generate(topology);
    }
    public static void generateGrid(Topology tp, int order){
        generateGrid(tp, order, order);
    }
    public static void generateGrid(Topology tp, int orderX, int orderY){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(50);
        gf.setWidth(tp.getWidth()-50);
        gf.setHeight(tp.getWidth()-50);
        gf.setWired(false);
        gf.setNodeClass(tp.getNodeModel("default"));
        gf.newGrid(orderX, orderY).generate(tp);

    }
    public static void generateTorus(Topology tp, int order){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(50);
        gf.setWidth(tp.getWidth()-50);
        gf.setHeight(tp.getWidth()-50);
        gf.setWired(false);
        gf.setNodeClass(tp.getNodeModel("default"));
        gf.newTorus(order, order).generate(tp);
    }
    public static void generateKN(Topology topology, int nbNodes){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(150);
        gf.setY(150);
        gf.setWidth(200);
        gf.setHeight(200.0);
        gf.setWired(false);
        gf.setNodeClass(topology.getNodeModel("default"));
        gf.setOrder (nbNodes);
        gf.newKN().generate(topology);
    }
    public static void generateTriangleGrid(Topology tp, int order){
        generateTriangleGrid(tp, order, order);
    }
    public static void generateTriangleGrid(Topology tp, int orderX, int orderY){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(50);
        gf.setWidth(tp.getWidth()-50);
        gf.setHeight(tp.getWidth()-50);
        gf.setWired(true);
        gf.setNodeClass(tp.getNodeModel("default"));
        gf.newTriangleGrid(orderX, orderY).generate(tp);

    }
}
