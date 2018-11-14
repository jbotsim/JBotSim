package io.jbotsim.io.serialization.topology.string.dot;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.MutablePortNode;
import guru.nidi.graphviz.parse.Parser;
import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.io.serialization.topology.string.TopologySerializer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by acasteig on 08/10/15.
 */
public class DotTopologySerializer implements TopologySerializer {
    double scale;

    public DotTopologySerializer() {
        this.scale = 2;
    }

    public DotTopologySerializer(double scale) {
        this.scale = scale;
    }

    @Override
    public void importTopology(Topology tp, String s) {
        tp.disableWireless();

        try {
            MutableGraph G = Parser.read(s);
            makeTopologyFromGraph(G, tp);
            organize(tp, scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String exportTopology(Topology tp) {
        return null;
    }

    private void makeTopologyFromGraph(MutableGraph G, Topology tp) {
        HashMap<MutableNode,Node> nodes = new HashMap<>();

        for(MutableNode mutableNode : G.nodes()) {
            Node node = tp.newInstanceOfModel("default");
            extractAttributes(mutableNode, node);
            tp.addNode(node);
            nodes.put(mutableNode, node);
        }
        Link.Type linkType = G.isDirected() ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
        for(MutableNode mutableNode : G.nodes()) {
            Node src = nodes.get(mutableNode);

            for (guru.nidi.graphviz.model.Link ml : mutableNode.links()) {
                MutablePortNode mpn = (MutablePortNode)ml.to();
                Node dst = nodes.get(mpn.node());
                assert (dst != null);
                Link l = new Link(src, dst, linkType);
                extractAttributes(ml, l);
                tp.addLink(l);
            }
        }
    }

    private void extractAttributes(MutableNode mn, Node node) {
        String pos = (String) mn.get("pos");
        if (pos != null) {
            String[] xy = pos.split(",");
            node.setLocation(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
        }

        String c = (String) mn.get("color");
        if (c != null) {
            try {
                node.setColor(Color.decode(c.substring(0)));
            } catch (NumberFormatException e) {

            }
        }
        c = (String) mn.get("fillcolor");
        if (c != null) {
            try {
                node.setColor(Color.decode(c.substring(0)));
            } catch (NumberFormatException e) {

            }
        }
    }

    private void extractAttributes(guru.nidi.graphviz.model.Link ml, Link l) {
        String c = (String) ml.get("color");
        if (c != null) {
            try {
                l.setColor(Color.decode(c.substring(0)));
            } catch (NumberFormatException e) {

            }
        }
        String w = (String) ml.get("width");
        if (w != null) {
            try {
                l.setWidth(Integer.decode(w));
            } catch (NumberFormatException e) {

            }
        }
    }

    public static void organize(Topology tp, double scale){
        if(tp.getNodes().isEmpty())
            return;

        // Applies Scale
        for (Node node : tp.getNodes())
            node.setLocation(node.getX() * scale, node.getY() * scale);

        // Adjust window size and centers the graph within
        final int margin = 50;
        double minX = Integer.MAX_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxX = 0;
        double maxY = 0;
        for (Node node : tp.getNodes()){
            if (node.getX() < minX)
                minX = node.getX();
            if (node.getX() > maxX)
                maxX = node.getX();
            if (node.getY() < minY)
                minY = node.getY();
            if (node.getY() > maxY)
                maxY = node.getY();
        }
        double width = (maxX - minX) + 2*margin;
        double height = (maxY - minY) + 2*margin;
        tp.setDimensions((int) width, (int) height);
        double xshift = margin - minX;
        double yshift = margin - minY;
        for (Node node : tp.getNodes())
            node.setLocation(node.getX() + xshift, node.getY() + yshift);

        // Flips Y-coordinate
        for (Node node : tp.getNodes())
            node.setLocation(node.getX(), height - node.getY());
    }

//    // Test
//    public static void main(String[] args) {
//        Topology tp = new Topology();
//        tp.disableWireless();
//        String filename = "/home/acasteig/test.io.jbotsim.io.serialization.topology.string.dot"; // to be updated
//        Format.importFromFile(tp, filename, new DotFormatter());
//        new JViewer(tp);
//    }

}
