package io.jbotsim.adapters.jgrapht.totopology;

import io.jbotsim.adapters.jgrapht.JGraphTAdapter;
import io.jbotsim.adapters.jgrapht.JGraphTAdapterTest;
import io.jbotsim.core.Topology;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;

class JGraphTAdapterDefaultUndirectGraphTest extends JGraphTAdapterTest {

    @Test
    void toTopology_weighted_ok() {
        DefaultUndirectedWeightedGraph graph = buildWeightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasWeights(topology);
    }

    @Test
    void toTopology_unweighted_ok() {
        DefaultUndirectedGraph graph = buildUnweightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasNoWeight(topology);
    }

    private void checkTopologyOverTestGraph1(Topology topology) {
        checkNbNodes(topology, 5);
        checkNbLinks(topology, 6);

        assertOneToAll(topology);
        assertAllToOne(topology);
    }

    private DefaultUndirectedWeightedGraph buildWeightedTestGraph1() {
        DefaultUndirectedWeightedGraph graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        graph.setVertexSupplier(getAnonymousVertexSupplier());

        // 5 anonymous vertexes
        // 6 edges
        // - v1 shares an edge with all others
        // - v1 loops
        populateTestGraph1(graph);

        return graph;
    }

    private DefaultUndirectedGraph buildUnweightedTestGraph1() {
        DefaultUndirectedGraph graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        graph.setVertexSupplier(getAnonymousVertexSupplier());

        // 5 anonymous vertexes
        // 6 edges
        // - v1 shares an edge with all others
        // - v1 loops
        populateTestGraph1(graph);

        return graph;
    }

}