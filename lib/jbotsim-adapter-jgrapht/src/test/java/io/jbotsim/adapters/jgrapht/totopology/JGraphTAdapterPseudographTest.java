package io.jbotsim.adapters.jgrapht.totopology;

import io.jbotsim.adapters.jgrapht.JGraphTAdapter;
import io.jbotsim.adapters.jgrapht.JGraphTAdapterTest;
import io.jbotsim.core.Topology;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;
import org.junit.jupiter.api.Test;

/**
 * As explained in <a href="https://jgrapht.org/guide/UserOverview#graph-structures">JGraphT documentation</a>,
 * the {@link Pseudograph} class allows for multiple edges to exist in the graph. This behavior is properly
 * taken into account by the adapter.
 */
class JGraphTAdapterPseudographTest extends JGraphTAdapterTest {

    @Test
    void toTopology_weighted_ok() {
        WeightedPseudograph graph = buildWeightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasWeights(topology);
    }

    @Test
    void toTopology_unweighted_ok() {
        Pseudograph graph = buildUnweightedTestGraph1();

        Topology topology = JGraphTAdapter.toTopology(graph);

        checkTopologyOverTestGraph1(topology);
        assertHasNoWeight(topology);
    }

    private void checkTopologyOverTestGraph1(Topology topology) {
        checkNbNodes(topology, 5);
        checkNbLinks(topology, 8);

        assertOneToAll(topology);
        assertAllToOne(topology);
    }


    private WeightedPseudograph buildWeightedTestGraph1() {
        WeightedPseudograph graph = new WeightedPseudograph<>(DefaultWeightedEdge.class);
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

    private Pseudograph buildUnweightedTestGraph1() {
        Pseudograph graph = new Pseudograph<>(DefaultEdge.class);
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