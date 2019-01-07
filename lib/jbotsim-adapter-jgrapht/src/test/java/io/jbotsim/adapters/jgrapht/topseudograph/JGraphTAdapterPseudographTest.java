package io.jbotsim.adapters.jgrapht.topseudograph;

import io.jbotsim.adapters.jgrapht.JGraphTAdapter;
import io.jbotsim.adapters.jgrapht.JGraphTAdapterTest;
import io.jbotsim.adapters.jgrapht.UnweightedLink;
import io.jbotsim.adapters.jgrapht.WeightedLink;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.jgrapht.graph.AbstractGraph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;
import org.junit.jupiter.api.Test;

class JGraphTAdapterPseudographTest extends JGraphTAdapterTest {

    @Test
    void toTopology_unweighted_ok() {
        Topology topology = buildTestTopology1();

        Pseudograph<Node, UnweightedLink> graph = JGraphTAdapter.toPseudograph(topology);

        checkGraphOverTestTopology1(graph);
        assertHasNotWeight(graph, 5);
    }

    private void checkGraphOverTestTopology1(AbstractGraph graph) {
        checkNbVertices(graph, 5);
        checkNbEdges(graph, 8);

        assertOneToAll(graph);
        assertAllToOne(graph);
    }

    protected Topology buildTestTopology1() {
        Topology topology = new Topology();
        topology.disableWireless();

        populateTestTopology1(topology, false);

        return topology;
    }

    @Test
    void toTopology_weighted_ok() {
        Topology topology = buildTestTopology1();

        WeightedPseudograph<Node, WeightedLink> graph = JGraphTAdapter.toWeightedPseudograph(topology);

        checkGraphOverTestTopology1(graph);
        assertHasWeight(graph, 5);
        assertHasNotWeight(graph, 12);
    }


}