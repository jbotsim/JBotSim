package io.jbotsim.topology;

import io.jbotsim.core.Topology;

public class TopologyGenerator {
    public static final String DEFAULT_NODE_MODEL = "default";
    
    public static void generateLine(Topology tp, int order){
        generateLine(tp, order, DEFAULT_NODE_MODEL);
    }

    public static void generateLine(Topology tp, int order, String nodeModel){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(100);
        gf.setWidth(tp.getWidth());
        gf.setHeight(tp.getHeight());
        gf.setWired(false);
        gf.setNodeClass(tp.getNodeModel(nodeModel));
        gf.setOrder (order);
        gf.newHorizontalLine().generate(tp);
    }

    public static void generateRing(Topology topology, int nbNodes) {
        generateRing(topology, nbNodes, false, DEFAULT_NODE_MODEL);
    }

    public static void generateRing(Topology topology, int nbNodes, String nodeModel) {
        generateRing(topology, nbNodes, false, nodeModel);
    }

    public static void generateRing(Topology topology, int nbNodes, boolean directed) {
        generateRing(topology, nbNodes, directed, DEFAULT_NODE_MODEL);
    }

    public static void generateRing(Topology topology, int nbNodes, boolean directed, String nodeModel) {
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(150);
        gf.setY(150);
        gf.setWidth(200);
        gf.setHeight(200.0);
        gf.setDirected(directed);
        gf.setWired(true);
        gf.setNodeClass(topology.getNodeModel(nodeModel));
        gf.setOrder (nbNodes);
        gf.newRing().generate(topology);
    }

    public static void generateGrid(Topology tp, int order) {
        generateGrid(tp, order, DEFAULT_NODE_MODEL);
    }
    
    public static void generateGrid(Topology tp, int order, String nodeModel){
        generateGrid(tp, order, order, nodeModel);
    }
    
    public static void generateGrid(Topology tp, int orderX, int orderY){
        generateGrid(tp, orderX, orderY, DEFAULT_NODE_MODEL);
    }

    public static void generateGrid(Topology tp, int orderX, int orderY, String nodeModel){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(50);
        gf.setWidth(tp.getWidth()-50);
        gf.setHeight(tp.getWidth()-50);
        gf.setWired(false);
        gf.setNodeClass(tp.getNodeModel(nodeModel));
        gf.newGrid(orderX, orderY).generate(tp);

    }
    public static void generateTorus(Topology tp, int order) {
        generateTorus(tp, order, DEFAULT_NODE_MODEL);
    }

    public static void generateTorus(Topology tp, int order, String nodeModel){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(50);
        gf.setY(50);
        gf.setWidth(tp.getWidth()-50);
        gf.setHeight(tp.getWidth()-50);
        gf.setWired(false);
        gf.setNodeClass(tp.getNodeModel(nodeModel));
        gf.newTorus(order, order).generate(tp);
    }
    public static void generateKN(Topology topology, int nbNodes){
        generateKN(topology, nbNodes, DEFAULT_NODE_MODEL);
    }

    public static void generateKN(Topology topology, int nbNodes, String nodeModel){
        TopologyGeneratorFactory gf = new TopologyGeneratorFactory();
        gf.setAbsoluteCoords(true);
        gf.setX(150);
        gf.setY(150);
        gf.setWidth(200);
        gf.setHeight(200.0);
        gf.setWired(false);
        gf.setNodeClass(topology.getNodeModel(nodeModel));
        gf.setOrder (nbNodes);
        gf.newKN().generate(topology);
    }
}
