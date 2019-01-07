package io.jbotsim.adapters.jgrapht.totopology;

import io.jbotsim.adapters.jgrapht.JGraphTAdapter;
import io.jbotsim.adapters.jgrapht.JGraphTAdapterTest;
import io.jbotsim.core.Topology;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;

class JGraphTAdapterDefaultDirectedGraphTest extends JGraphTAdapterTest {

    @Test
    void toTopology_weighted_ok() {
        DefaultDirectedWeightedGraph graph = buildWeightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasWeights(topology);
    }

    @Test
    void toTopology_unweighted_ok() {
        DefaultDirectedGraph graph = buildUnweightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasNoWeight(topology);
    }

    private void checkTopologyOverTestGraph1(Topology topology) {
        checkNbNodes(topology, 5);
        checkNbArcs(topology, 6);

        assertOneToAll(topology);
        assertNotAllToOne(topology);
    }

    private DefaultDirectedWeightedGraph buildWeightedTestGraph1() {
        DefaultDirectedWeightedGraph graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        graph.setVertexSupplier(getAnonymousVertexSupplier());

        // 5 anonymous vertexes
        // 6 edges
        // - v1 shares an edge with all others
        // - v1 loops
        populateTestGraph1(graph);

        return graph;
    }

    private DefaultDirectedGraph buildUnweightedTestGraph1() {
        DefaultDirectedGraph graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        graph.setVertexSupplier(getAnonymousVertexSupplier());

        // 5 anonymous vertexes
        // 6 edges
        // - v1 shares an edge with all others
        // - v1 loops
        populateTestGraph1(graph);

        return graph;
    }

}