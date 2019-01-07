package io.jbotsim.adapters.jgrapht.topseudograph;

import io.jbotsim.adapters.jgrapht.JGraphTAdapter;
import io.jbotsim.adapters.jgrapht.JGraphTAdapterTest;
import io.jbotsim.adapters.jgrapht.UnweightedLink;
import io.jbotsim.adapters.jgrapht.WeightedLink;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.jgrapht.graph.AbstractGraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.junit.jupiter.api.Test;

class JGraphTAdapterDirectedPseudographTest extends JGraphTAdapterTest {

    @Test
    void toTopology_unweighted_ok() {
        Topology topology = buildTestTopology1();

        DirectedPseudograph<Node, UnweightedLink> graph = JGraphTAdapter.toDirectedPseudograph(topology);

        checkGraphOverTestTopology1(graph);
        assertHasNotWeight(graph, 5);
    }

    private void checkGraphOverTestTopology1(AbstractGraph graph) {
        checkNbVertices(graph, 5);
        checkNbEdges(graph, 8);

        assertOneToAll(graph);
        assertNotAllToOne(graph);
    }

    protected Topology buildTestTopology1() {
        Topology topology = new Topology();
        topology.disableWireless();

        populateTestTopology1(topology, true);

        return topology;
    }

    @Test
    void toTopology_weighted_ok() {
        Topology topology = buildTestTopology1();

        DirectedWeightedPseudograph<Node, WeightedLink> graph = JGraphTAdapter.toDirectedWeightedPseudograph(topology);

        checkGraphOverTestTopology1(graph);
        assertHasWeight(graph, 5);
        assertHasNotWeight(graph, 12);
    }


}