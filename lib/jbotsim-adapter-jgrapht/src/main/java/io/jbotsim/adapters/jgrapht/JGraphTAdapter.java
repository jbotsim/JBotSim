package io.jbotsim.adapters.jgrapht;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>The {@link JGraphTAdapter} proposes a bridge between JBotSim's {@link Topology} and several graph types
 * available in <a href="https://jgrapht.org/">JGraphT</a>.</p>
 *
 * <p>By default, the {@link Topology} allows:</p>
 * <ul>
 *     <li>directed/undirected links,</li>
 *     <li>multiple links,</li>
 *     <li>weighted/unweighted links,</li>
 *     <li>loops.</li>
 * </ul>
 * <p>Thus, the following types are to be used: {@link Pseudograph}, {@link DirectedPseudograph},
 * {@link WeightedPseudograph} and {@link DirectedWeightedPseudograph} are to be used.</p>
 * <p>The JGraphT graphs returned use {@link Node}s as vertices and {@link UnweightedLink}s or {@link WeightedLink} as
 * edges.</p>
 */
public class JGraphTAdapter {
    /**
     * The key of the property under which the weight is stored in the {@link Link}.
     */
    public static final String WEIGHT_PROPERTY_NAME = "label";
    /**
     * The default value to use for a {@link Link} weight.
     */
    public static final int DEFAULT_WEIGHT = 1;

    // region to JGraphT

    /**
     * <p>Creates a {@link Pseudograph} from a {@link Topology}.</p>
     * <p>Note: the {@link Node}s and {@link Link}s present in the {@link Topology} are referenced as is in the
     * resulting graph.</p>
     *
     * @param topology the {@link Topology} to use as source
     * @return a {@link Pseudograph} with {@link Node}s as vertices and {@link UnweightedLink} as edges.
     */
    public static Pseudograph<Node, UnweightedLink> toPseudograph(Topology topology) {

        Pseudograph<Node, UnweightedLink> graph = (Pseudograph<Node, UnweightedLink>) createUnweightedPseudograph(false);

        populateNodes(topology, graph);
        JGraphTAdapter.populateUnweightedEdges(topology, graph);

        return graph;
    }

    /**
     * <p>Creates a {@link WeightedPseudograph} from a {@link Topology}.</p>
     * <p>Note: the {@link Node}s and {@link Link}s present in the {@link Topology} are referenced as is in the
     * resulting graph.</p>
     *
     * @param topology the {@link Topology} to use as source
     * @return a {@link WeightedPseudograph} with {@link Node}s as vertices and {@link WeightedLink} as edges.
     */
    public static WeightedPseudograph<Node, WeightedLink> toWeightedPseudograph(Topology topology) {
        WeightedPseudograph<Node, WeightedLink> graph = (WeightedPseudograph<Node, WeightedLink>)
                createWeightedPseudographFor(false);

        populateNodes(topology, graph);
        populateWeightedEdges(topology, graph);

        return graph;
    }

    /**
     * <p>Creates a {@link DirectedPseudograph} from a {@link Topology}.</p>
     * <p>Note: the {@link Node}s and {@link Link}s present in the {@link Topology} are referenced as is in the
     * resulting graph.</p>
     *
     * @param topology the {@link Topology} to use as source
     * @return a {@link DirectedPseudograph} with {@link Node}s as vertices and {@link UnweightedLink} as edges.
     */
    public static DirectedPseudograph<Node, UnweightedLink> toDirectedPseudograph(Topology topology) {
        DirectedPseudograph<Node, UnweightedLink> graph = (DirectedPseudograph<Node, UnweightedLink>)
                createUnweightedPseudograph(true);

        populateNodes(topology, graph);
        populateUnweightedEdges(topology, graph);

        return graph;
    }

    /**
     * <p>Creates a {@link DirectedWeightedPseudograph} from a {@link Topology}.</p>
     * <p>Note: the {@link Node}s and {@link Link}s present in the {@link Topology} are referenced as is in the
     * resulting graph.</p>
     *
     * @param topology the {@link Topology} to use as source
     * @return a {@link DirectedWeightedPseudograph} with {@link Node}s as vertices and {@link WeightedLink} as edges.
     */
    public static DirectedWeightedPseudograph<Node, WeightedLink> toDirectedWeightedPseudograph(Topology topology) {
        DirectedWeightedPseudograph<Node, WeightedLink> graph = (DirectedWeightedPseudograph<Node, WeightedLink>)
            createWeightedPseudographFor(true);

        populateNodes(topology, graph);
        populateWeightedEdges(topology, graph);

        return graph;
    }

    /**
     * <p>Creates a {@link Pseudograph}.</p>
     * <p>Depending on the provided parameter, the result should be casted into a {@link DirectedPseudograph}
     * (<code>true</code>) or a {@link Pseudograph} (<code>false</code>).</p>
     * @param directed <code>true</code> if the resulting graph should be directed; <code>false</code> otherwise.
     * @return a {@link Pseudograph} or a {@link DirectedPseudograph} (depending on the directed parameter) with
     * {@link Node}s as vertices and {@link UnweightedLink} as edges.
     */
    private static AbstractGraph<Node, UnweightedLink> createUnweightedPseudograph(boolean directed) {

        GraphTypeBuilder<Node, UnweightedLink> graphTypeBuilder = directed ?
                GraphTypeBuilder.directed():
                GraphTypeBuilder.undirected();

        graphTypeBuilder.allowingMultipleEdges(true)
                .allowingSelfLoops(true)
                .weighted(false)
                .edgeClass(UnweightedLink.class);
        Graph<Node, UnweightedLink> graph = graphTypeBuilder.buildGraph();

        return (AbstractGraph<Node, UnweightedLink>) graph;
    }

    /**
     * <p>Creates a {@link WeightedPseudograph}.</p>
     * <p>Depending on the provided parameter, the result should be casted into a {@link DirectedWeightedPseudograph}
     * (<code>true</code>) or a {@link WeightedPseudograph} (<code>false</code>).</p>
     * @param directed <code>true</code> if the resulting graph should be directed; <code>false</code> otherwise.
     * @return a {@link WeightedPseudograph} or a {@link DirectedWeightedPseudograph} (depending on the directed parameter) with
     * {@link Node}s as vertices and {@link WeightedLink} as edges.
     */
    private static AbstractGraph<Node, WeightedLink> createWeightedPseudographFor(boolean directed) {

        GraphTypeBuilder<Node, WeightedLink> graphTypeBuilder = directed ?
                GraphTypeBuilder.directed():
                GraphTypeBuilder.undirected();

        graphTypeBuilder.allowingMultipleEdges(true)
                .allowingSelfLoops(true)
                .weighted(true)
                .edgeClass(WeightedLink.class);
        Graph<Node, WeightedLink> graph = graphTypeBuilder.buildGraph();

        return (AbstractGraph<Node, WeightedLink>) graph;
    }

    /**
     * <p>Populates the vertexSet of the destGraph with the {@link Node}s from the provided {@link Topology}.</p>
     * @param topology the source {@link Topology}.
     * @param destGraph the destination {@link AbstractGraph}.
     */
    private static void populateNodes(Topology topology, AbstractGraph<Node, ?> destGraph) {
        for(Node n: topology.getNodes())
            destGraph.addVertex(n);
    }


    /**
     * <p>Populates the edgeSet of the destGraph with the {@link Link}s from the provided {@link Topology}.</p>
     * <p>Possible weights on the source {@link Topology} are not taken into account.</p>
     * @param topology the source {@link Topology}.
     * @param destGraph the destination {@link AbstractGraph}.
     */
    private static void populateUnweightedEdges(Topology topology, AbstractGraph<Node, UnweightedLink> destGraph) {
        for (Link l : topology.getLinks(topology.hasDirectedLinks())) {
            UnweightedLink link = new UnweightedLink(l);
            destGraph.addEdge(l.source, l.destination, link);
        }
    }

    /**
     * <p>Populates the edgeSet of the destGraph with the {@link Link}s from the provided {@link Topology}.</p>
     * <p>Possible weights on the source {@link Topology} are taken into account, using {@link WeightedLink}s.</p>
     * @param topology the source {@link Topology}.
     * @param destGraph the destination {@link AbstractGraph}.
     */
    private static void populateWeightedEdges(Topology topology, AbstractGraph<Node, WeightedLink> destGraph) {
        for (Link l : topology.getLinks(topology.hasDirectedLinks())) {
            WeightedLink link = new WeightedLink(l);
            destGraph.addEdge(l.source, l.destination, link);
            destGraph.setEdgeWeight(link, getLinkWeight(l));
        }
    }

    /**
     * <p>Retrieve the weight of a {@link Link}.</p>
     * <p>In the {@link Link}, the weight should be a {@link Double} under the property name
     * {@link #WEIGHT_PROPERTY_NAME} ({@value WEIGHT_PROPERTY_NAME}). If not found, {@link #DEFAULT_WEIGHT}
     * ({@value DEFAULT_WEIGHT}) is returned.</p>
     * @param link the {@link Link} to be used.
     * @return the double corresponding to the {@link Link}'s weight. The default value is {@value DEFAULT_WEIGHT}.
     */
    private static double getLinkWeight(Link link) {
        double weight = DEFAULT_WEIGHT;
        if(link.hasProperty(WEIGHT_PROPERTY_NAME))
            weight = (double) link.getProperty(WEIGHT_PROPERTY_NAME);

        return weight;
    }


    // endregion


    // region from JGraphT
    /**
     * <p>Creates a {@link Topology} from a {@link DefaultUndirectedGraph}.</p>
     * <p>Depending on the provided graph, the resulting {@link Topology} can contain:</p>
     * <ul>
     *     <li>self-loops;</li>
     *     <li>weighted edges as a property (using {@value #WEIGHT_PROPERTY_NAME} as property key).</li>
     * </ul>
     * <p>As described in the <a href="https://jgrapht.org/guide/UserOverview#graph-structures">JGraphT
     * documentation</a></p>, multiple edges are not allowed.
     *
     * @param graph the {@link DefaultUndirectedGraph} to turn into a {@link Topology}.
     * @return the resulting {@link Topology}.
     */
    public static Topology toTopology(DefaultUndirectedGraph graph) {
        return getTopology(graph);
    }

    /**
     * <p>Creates a {@link Topology} from a {@link DefaultDirectedGraph}.</p>
     * <p>Depending on the provided graph, the resulting {@link Topology} can contain:</p>
     * <ul>
     *     <li>self-loops;</li>
     *     <li>weighted arcs as a property (using {@value #WEIGHT_PROPERTY_NAME} as property key).</li>
     * </ul>
     * <p>As described in the <a href="https://jgrapht.org/guide/UserOverview#graph-structures">JGraphT
     * documentation</a></p>, multiple arcs are not allowed.
     *
     * @param graph the {@link DefaultDirectedGraph} to turn into a {@link Topology}.
     * @return the resulting {@link Topology}.
     */
    public static Topology toTopology(DefaultDirectedGraph graph) {
        return getTopology(graph);
    }

    /**
     * <p>Creates a {@link Topology} from a {@link Pseudograph}.</p>
     * <p>Depending on the provided graph, the resulting {@link Topology} can contain:</p>
     * <ul>
     *     <li>self-loops;</li>
     *     <li>weighted edges as a property (using {@value #WEIGHT_PROPERTY_NAME} as property key);</li>
     *     <li>multiple edges.</li>
     * </ul>
     * @param graph the {@link Pseudograph} to turn into a {@link Topology}.
     * @return the resulting {@link Topology}.
     */
    public static Topology toTopology(Pseudograph graph) {
        return getTopology(graph);
    }

    /**
     * <p>Creates a {@link Topology} from a {@link DirectedPseudograph}.</p>
     * <p>Depending on the provided graph, the resulting {@link Topology} can contain:</p>
     * <ul>
     *     <li>self-loops;</li>
     *     <li>weighted arcs as a property (using {@value #WEIGHT_PROPERTY_NAME} as property key);</li>
     *     <li>multiple arcs.</li>
     * </ul>
     * @param graph the {@link DirectedPseudograph} to turn into a {@link Topology}.
     * @return the resulting {@link Topology}.
     */
    public static Topology toTopology(DirectedPseudograph graph) {
        return getTopology(graph);
    }

    /**
     * Common code to turn an {@link AbstractGraph} into a {@link Topology}.
     *
     * @param graph the {@link AbstractGraph} to turn into a {@link Topology}.
     * @return the resulting {@link Topology}.
     */
    private static Topology getTopology(AbstractGraph graph) {
        Topology tp = new Topology();
        tp.disableWireless();
        tp.clear();

        Map<Object, Node> vertexToNode = new HashMap<>();

        for (Object vertex : graph.vertexSet()) {
            Node node = new Node();
            tp.addNode(node);
            vertexToNode.put(vertex, node);
        }

        for (Object edge : graph.edgeSet()) {

            double edgeWeight = graph.getEdgeWeight(edge);
            Object sourceVertex = graph.getEdgeSource(edge);
            Object targetVertex = graph.getEdgeTarget(edge);

            Node sourceNode = getNodeForVertex(vertexToNode, sourceVertex);
            Node destinationNode = getNodeForVertex(vertexToNode, targetVertex);

            Link link;
            if(graph.getType().isDirected())
                link = createDirectedLink(sourceNode, destinationNode);
            else
                link = createUndirectedLink(sourceNode, destinationNode);

            if(graph.getType().isWeighted())
                setWeight(link, edgeWeight);

            tp.addLink(link);
        }

        return tp;
    }

    private static void setWeight(Link link, double weight) {
        link.setProperty(WEIGHT_PROPERTY_NAME, weight);
    }

    private static Link createUndirectedLink(Node sourceNode, Node destinationNode) {
        return new Link(sourceNode, destinationNode, Link.Type.UNDIRECTED, Link.Mode.WIRED);
    }
    private static Link createDirectedLink(Node sourceNode, Node destinationNode) {
        return new Link(sourceNode, destinationNode, Link.Type.DIRECTED, Link.Mode.WIRED);
    }

    private static Node getNodeForVertex(Map<Object, Node> vertexToNode, Object vertex) {
        return vertexToNode.get(vertex);
    }

    // endregion
}
