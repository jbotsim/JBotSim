package examples.basic.links;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.List;

/**
 * This examples adds a bunch of random nodes and some directed links between some of them.
 * The result is displayed with a JViewer.
 */
public class DirectedLinksMain {

    public static final int MAX_NODE_ID = 50;

    public static void main(String[] args) {
        Topology tp = new Topology();

        tp.setDefaultNodeModel(Node.class);
        tp.disableWireless();

        deployRandomNodes(tp, MAX_NODE_ID);

        addRandomDirectedLinks(tp,5, MAX_NODE_ID);

        new JViewer(tp);
        tp.start();

    }

    /**
     * Randomly deploy maxNodeId {@link Node}s in the {@link Topology}.
     *
     * @param tp the {@link Topology}
     * @param maxNodeId the upper bound of the identifiers available for {@link Node}s creation
     */
    private static void deployRandomNodes(Topology tp, int maxNodeId) {
        for (int i = 0; i < maxNodeId; i++){
            Node node = new Node();
            node.setLocation(getRandomX(tp), getRandomY(tp));
            node.setID(i);
            tp.addNode(node);
        }
    }

    private static double getRandomX(Topology tp) {
        return Math.random() * tp.getWidth();
    }
    private static double getRandomY(Topology tp) {
        return Math.random() * tp.getHeight();
    }

    /**
     * Randomly add directed {@link Link}s between existing {@link Node}s in the {@link Topology}.
     * 
     * @param tp the {@link Topology}
     * @param nbLinks the number of links to add
     * @param maxNodeId the upper bound of {@link Node} identifiers available in the {@link Topology}
     */
    private static void addRandomDirectedLinks(Topology tp, int nbLinks, int maxNodeId) {
        for(int i = 0; i < nbLinks; i++){
            Node from, to;
            do {
                from = pickRandomNode(tp, maxNodeId);
                to = pickRandomNode(tp, maxNodeId);
            } while (!isFitForNewDirectedLink(tp, from, to));

            addDirectedLink(tp, from, to);
        }
    }

    private static void addDirectedLink(Topology tp, Node from, Node to) {
        tp.addLink(new Link(from, to, Link.Type.DIRECTED, Link.Mode.WIRED));
    }

    private static boolean isFitForNewDirectedLink(Topology tp, Node from, Node to) {
        if(from == to)
            return false;

        if(tp.getLink(from, to, true) != null)
            return false;

        return true;
    }

    private static Node pickRandomNode(Topology tp, int maxNodeId) {
        int randomIndex = (int) (Math.random() * maxNodeId);
        for(Node n : tp.getNodes())
            if(n.getID() == randomIndex)
                return n;

        return null;
    }

}
