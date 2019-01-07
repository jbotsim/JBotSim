package io.jbotsim.adapters.jgrapht;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.jgrapht.Graphs;
import org.jgrapht.graph.AbstractGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link JGraphTAdapter}.
 */
public class JGraphTAdapterTest {
    public static final String WEIGHT_PROPERTY_KEY = "label";

    /**
     * Tries to populate the provided {@link AbstractGraph} with the arbitrary "TestGraph1".
     *
     * According to the real type of the provided graph the resulting graph might contain some loops and duplicate edges.
     *
     * @param graph the {@link AbstractGraph} to be populated
     */
    protected void populateTestGraph1(AbstractGraph graph) {
        // 5 anonymous vertexes
        Object v1 = graph.addVertex();
        Object v2 = graph.addVertex();
        Object v3 = graph.addVertex();
        Object v4 = graph.addVertex();
        Object v5 = graph.addVertex();

        // 8 edges
        // v1 shares an edge with all others
        // v1 loops
        // v1 loop duplicated
        // v2-v5 edge duplicated
        DefaultWeightedEdge e0 = addEdge(graph, v1, v1, 0);
        DefaultWeightedEdge e1 = addEdge(graph, v1, v2, 1);
        DefaultWeightedEdge e2 = addEdge(graph, v1, v3, 2);
        DefaultWeightedEdge e3 = addEdge(graph, v1, v4, 3);
        DefaultWeightedEdge e4 = addEdge(graph, v1, v5, 4);
        DefaultWeightedEdge e5 = addEdge(graph, v2, v5, 5);

        DefaultWeightedEdge e6_e0_loop_copied = addEdge(graph, v1, v1, 0);
        DefaultWeightedEdge e7_e5_edge_copied = addEdge(graph, v2, v5, 5);
    }

    protected DefaultWeightedEdge addEdge(AbstractGraph graph, Object v1, Object v2, double weight) {
        DefaultWeightedEdge e1 = new DefaultWeightedEdge();

        graph.addEdge(v1, v2, e1);
        if(graph.getType().isWeighted())
            graph.setEdgeWeight(e1, weight);

        return e1;
    }

    protected Supplier getAnonymousVertexSupplier() {
        return Object::new;
    }

    /**
     * Populates the provided {@link Topology} with the arbitrary "TestGraph1".
     *
     * @param topology the {@link Topology} to populate
     */
    protected void populateTestTopology1(Topology topology, boolean directed) {
        // 5 anonymous Nodes
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Node n4 = new Node();
        Node n5 = new Node();
        topology.addNode(n1);
        topology.addNode(n2);
        topology.addNode(n3);
        topology.addNode(n4);
        topology.addNode(n5);

        // 8 links
        // v1 shares a link with all others
        // v1 loops
        // v1 loop duplicated
        // v2-v5 link duplicated
        Link l0 = addLink(topology, n1, n1, 0, directed);
        Link l1 = addLink(topology, n1, n2, 1, directed);
        Link l2 = addLink(topology, n1, n3, 2, directed);
        Link l3 = addLink(topology, n1, n4, 3, directed);
        Link l4 = addLink(topology, n1, n5, 4, directed);
        Link l5 = addLink(topology, n2, n5, 5, directed);

        Link l6_l0_loop_copied = addLink(topology, n1, n1, 0, directed);
        Link l7_l5_edge_copied = addLink(topology, n2, n5, 5, directed);

    }

    protected Link addLink(Topology topology, Node n1, Node n2, double weight, boolean directed) {
        Link.Type type = directed ? Link.Type.DIRECTED : Link.Type.UNDIRECTED;
        Link link = new Link(n1, n2, type, Link.Mode.WIRED);
        link.setProperty(WEIGHT_PROPERTY_KEY, weight);

        topology.addLink(link);

        return link;
    }

    // region Topology
    protected void checkNbNodes(Topology topology, int expected) {
        assertEquals(expected, topology.getNodes().size());
    }

    protected void checkNbLinks(Topology topology, int expected) {
        assertEquals(expected, topology.getLinks(false).size());
    }

    protected void checkNbArcs(Topology topology, int expected) {
        assertEquals(expected, topology.getLinks(true).size());
    }

    protected void assertOneToAll(Topology topology) {
        assertTrue(TestTopologyHelper.hasOneToAll(topology));
    }

    protected void assertAllToOne(Topology topology) {
        assertTrue(TestTopologyHelper.hasAllToOneTo(topology));
    }

    protected void assertNotAllToOne(Topology topology) {
        assertFalse(TestTopologyHelper.hasAllToOneTo(topology));
    }

    protected void assertHasWeights(Topology topology) {
        assertTrue(TestTopologyHelper.hasWeights(topology));
    }

    protected void assertHasNoWeight(Topology topology) {
        assertFalse(TestTopologyHelper.hasWeights(topology));
    }

    static class TestTopologyHelper {

        /**
         * Checks that at least one {@link Node} in the {@link Topology} shares a link to all the others (and a loop with
         * itself).
         * @param topology the {@link Topology} to be tested
         * @return <tt>true</tt> if a {@link Node} can reach all the others; <tt>false</tt> otherwise.
         */
        static public boolean hasOneToAll(Topology topology) {
            for(Node node: topology.getNodes()) {
                if(oneToAll(topology, node))
                    return true;
            }

            return false;
        }

        /**
         * Checks that the specified {@link Node} can reach all the others in the {@link Topology} (supposes a loop to
         * itself).
         * @param topology the {@link Topology} to be tested
         * @param node the {@link Node} to be tested
         * @return <tt>true</tt> if the {@link Node} can reach all the others; <tt>false</tt> otherwise.
         */
        static private boolean oneToAll(Topology topology, Node node) {
            for(Node n : topology.getNodes())
                if(!nodeHasOutLinkTo(node, n))
                    return false;

            return true;
        }

        /**
         * Checks that the two provided {@link Node}s share a {@link Link}.
         * @param n1 the first {@link Node}
         * @param n2 the second {@link Node}
         * @return <tt>true</tt> if n2 is the destination of at least one {@link Link} in n1's outlinks; <tt>false</tt>
         * otherwise.
         */
        static private boolean nodeHasOutLinkTo(Node n1, Node n2) {
            for(Link link: n1.getOutLinks())
                if(link.destination == n2)
                    return true;

            return false;
        }


        static private boolean hasAllToOneTo(Topology topology) {
            for(Node node: topology.getNodes()) {
                if(allToOne(topology, node))
                    return true;
            }

            return false;
        }

        static private boolean allToOne(Topology topology, Node node) {
            for(Node n : topology.getNodes())
                if(!nodeHasOutLinkTo(n, node))
                    return false;

            return true;
        }


        static protected boolean hasWeights(Topology topology) {
            if(topology.hasDirectedLinks())
                return hasWeights(topology.getLinks(true));
            else
                return hasWeights(topology.getLinks(false));
        }

        static protected boolean hasWeights(List<Link> links) {
            for (Link l : links)
                if (isWeighted(l, WEIGHT_PROPERTY_KEY))
                    return true;
            return false;
        }

        static private boolean isWeighted(Link link, String weightPropertyKey) {
            return link.hasProperty(weightPropertyKey);
        }
    }

    // endregion

    // region AbstractGraph
    protected void assertOneToAll(AbstractGraph graph) {
        assertTrue(TestJGraphTHelper.hasOneToAll(graph));
    }

    protected void assertAllToOne(AbstractGraph graph) {
        assertTrue(TestJGraphTHelper.hasAllToOne(graph));
    }
    protected void assertNotAllToOne(AbstractGraph graph) {
        assertFalse(TestJGraphTHelper.hasAllToOne(graph));
    }

    protected void checkNbVertices(AbstractGraph graph, int expected) {
        assertEquals(expected, graph.vertexSet().size());
    }
    protected void checkNbEdges(AbstractGraph graph, int expected) {
        assertEquals(expected, graph.edgeSet().size());
    }

    protected void assertHasWeight(AbstractGraph graph, double expectedWeight) {
        assertTrue(TestJGraphTHelper.hasWeight(graph, expectedWeight));
    }
    protected void assertHasNotWeight(AbstractGraph graph, double expectedWeight) {
        assertFalse(TestJGraphTHelper.hasWeight(graph, expectedWeight));
    }

    static class TestJGraphTHelper {

        static boolean hasOneToAll(AbstractGraph graph) {
            for(Object vertex: graph.vertexSet())
                if(Graphs.successorListOf(graph, vertex).containsAll(graph.vertexSet()))
                    return true;

            return false;
        }

        static boolean hasAllToOne(AbstractGraph graph) {
            for(Object vertex: graph.vertexSet())
                if(Graphs.predecessorListOf(graph, vertex).containsAll(graph.vertexSet()))
                    return true;

            return false;
        }

        /**
         * Returns <code>true</code> if the provided graph contains at least one edge with matching weight.
         * @param graph the {@link WeightedPseudograph} to be tested
         * @param weight the expected weight to be found
         * @return <code>true</code> if the provided graph contains at least one edge with matching weight;
         * <code>false</code> otherwise.
         */
        static boolean hasWeight(AbstractGraph graph, double weight) {
            for(Object edge: graph.edgeSet())
                if(graph.getEdgeWeight(edge) == weight)
                    return true;
            return false;
        }
    }
    // endregion
}