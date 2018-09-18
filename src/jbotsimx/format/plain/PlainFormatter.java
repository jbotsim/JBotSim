package jbotsimx.format.plain;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Point;
import jbotsim.Topology;
import jbotsimx.format.common.Format;
import jbotsimx.format.common.Formatter;
import jbotsimx.format.dot.DotFormatter;
import jbotsimx.format.tikz.TikzFormatter;
import jbotsimx.ui.JViewer;

import java.util.HashMap;

public class PlainFormatter implements Formatter {
    public void importTopology(Topology tp, String s){
        tp.clear();
        tp.setCommunicationRange(Double.parseDouble(s.substring(s.indexOf(" ") + 1, s.indexOf("\n"))));
        s = s.substring(s.indexOf("\n") + 1);
        tp.setSensingRange(Double.parseDouble(s.substring(s.indexOf(" ") + 1, s.indexOf("\n"))));
        s = s.substring(s.indexOf("\n") + 1);
        HashMap<String, Node> nodeTable = new HashMap<>();
        while (s.indexOf("[") > 0) {
            double x = new Double(s.substring(s.indexOf("x") + 3, s.indexOf(", y")));
            double y = 0;
            double z = 0;
            if (s.contains("z")) {
                y = new Double(s.substring(s.indexOf("y") + 3, s.indexOf(", z")));
                z = new Double(s.substring(s.indexOf("z") + 3, s.indexOf("]")));
            }else{
                y = new Double(s.substring(s.indexOf("y") + 3, s.indexOf("]")));
            }
            try {
                Node node = tp.getDefaultNodeModel().newInstance();
                node.setLocation(x, y, z);
                tp.addNode(node);
                String id = s.substring(0, s.indexOf(" "));
                node.setProperty("id", id);
                nodeTable.put(id, node);
                s = s.substring(s.indexOf("\n") + 1);
            } catch (Exception e) {}
        }
        while (s.indexOf("--") > 0) {
            Node n1 = nodeTable.get(s.substring(0, s.indexOf(" ")));
            Node n2 = nodeTable.get(s.substring(s.indexOf(">") + 2, s.indexOf("\n")));
            Link.Type type = (s.indexOf("<") > 0 && s.indexOf("<") < s.indexOf("\n")) ? Link.Type.UNDIRECTED : Link.Type.DIRECTED;
            tp.addLink(new Link(n1, n2, type, Link.Mode.WIRED));
            s = s.substring(s.indexOf("\n") + 1);
        }
    }
    public String exportTopology(Topology tp){
        StringBuffer res = new StringBuffer();
        res.append("cR " + tp.getCommunicationRange() + "\n");
        res.append("sR " + tp.getSensingRange() + "\n");
        for (Node n : tp.getNodes()) {
            Point p2d = new Point();
            p2d.setLocation(n.getLocation().getX(), n.getLocation().getY());
            res.append(n.toString() + " " + p2d.toString().substring(p2d.toString().indexOf("[") -1) + "\n");
        }
        for (Link l : tp.getLinks())
            if (!l.isWireless())
                res.append(l.toString() + "\n");
        return res.toString();
    }

    // Test
    public static void main(String[] args) {
        String filename = "/home/acasteig/test.plain"; // to be updated
        Topology tp = Format.importFromFile(filename, new PlainFormatter());
        new JViewer(tp);
    }
}
