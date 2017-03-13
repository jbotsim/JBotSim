package jbotsimx.dot;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by acasteig on 08/10/15.
 */
public class Import {
    public static void importGraph(Topology tp, String filename, double scale){
        tp.disableWireless();
        parseNodes(tp, filename);
        parseLinks(tp, filename);
        organize(tp, scale);
    }
    protected static void parseNodes(Topology tp, String filename){
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            String line = input.readLine();
            while (line != null){
                if (line.contains("pos")) {
                    int comma = line.indexOf(",");
                    String id = line.substring(0, line.indexOf(" "));
                    String x = line.substring(line.indexOf("\"")+1, comma);
                    String y = line.substring(comma+1, line.indexOf("\"", comma));
                    Node n1 = tp.getDefaultNodeModel().newInstance();
                    n1.setID(Integer.parseInt(id));
                    n1.setLocation(Double.parseDouble(x), Double.parseDouble(y));
                    tp.addNode(n1);
                }
                line = input.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected static void parseLinks(Topology tp, String filename){
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            String line = input.readLine();
            while (line != null){
                if (line.contains("--")) {
                    int indexDash = line.indexOf("-");
                    int id1 = Integer.parseInt(line.substring(0, indexDash));
                    int id2 = Integer.parseInt(line.substring(indexDash + 2, line.indexOf(";")));
                    Node n1 = tp.findNodeById(id1);
                    Node n2 = tp.findNodeById(id2);
                    tp.addLink(new Link(n1, n2));
                }
                line = input.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void organize(Topology tp, double scale){
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
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.disableWireless();
        importGraph(tp, "/home/acasteig/test.dot", 2);
        new JViewer(tp);
    }
}
