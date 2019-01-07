package io.jbotsim.adapters.jgrapht.totopology;

import io.jbotsim.adapters.jgrapht.JGraphTAdapter;
import io.jbotsim.adapters.jgrapht.JGraphTAdapterTest;
import io.jbotsim.core.Topology;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.junit.jupiter.api.Test;

/**
 * As explained in <a href="https://jgrapht.org/guide/UserOverview#graph-structures">JGraphT documentation</a>,
 * the {@link DirectedPseudograph} class allows for multiple edges to exist in the graph. This behavior is
 * properly taken into account by the adapter.
 */
class JGraphTAdapterDirectedPseudographTest extends JGraphTAdapterTest {

    @Test
    void toTopology_weighted_ok() {
        DirectedWeightedPseudograph graph = buildWeightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasWeights(topology);
    }

    @Test
    void toTopology_unweighted_ok() {
        DirectedPseudograph graph = buildUnweightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasNoWeight(topology);
    }

    private void checkTopologyOverTestGraph1(Topology topology) {
        checkNbNodes(topology, 5);
        checkNbArcs(topology, 8);

        assertOneToAll(topology);
        assertNotAllToOne(topology);
    }


    private DirectedWeightedPseudograph buildWeightedTestGraph1() {
        DirectedWeightedPseudograph graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
        graph.setVertexSupplier(getAnonymousVertexSupplier());

        // 5 anonymous vertexes
        // 8 edges:
        // - v1 shares an edge with all others
        // - v1 loops
        // - v1 loop duplicated
        // - v2-v5 edge duplicated
        populateTestGraph1(graph);

        return graph;
    }

    private DirectedPseudograph buildUnweightedTestGraph1() {
        DirectedPseudograph graph = new DirectedPseudograph<>(DefaultWeightedEdge.class);
        graph.setVertexSupplier(getAnonymousVertexSupplier());

        // 5 anonymous vertexes
        // 8 edges:
        // - v1 shares an edge with all others
        // - v1 loops
        // - v1 loop duplicated
        // - v2-v5 edge duplicated
        populateTestGraph1(graph);

        return graph;
    }



}