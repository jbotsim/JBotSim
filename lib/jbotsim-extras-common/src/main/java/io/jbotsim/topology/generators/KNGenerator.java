package io.jbotsim.topology.generators;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

/**
 * The {@link KNGenerator} is a {@link TopologyGenerator} used to created complete-graphs shaped as rings.
 */
public class KNGenerator extends RingGenerator {

    /**
     * Creates a {@link KNGenerator} creating a complete-graph of nbNodes {@link Node}s.
     *
     * @param nbNodes the number of {@link Node}s to be added.
     */
    public KNGenerator(int nbNodes) {
        super(nbNodes);
    }

    @Override
    protected void addLinks(Topology tp, int nbNodes, Node[] nodes) {

        if (wired) {
            for (int i = 0; i < nbNodes; i++)
                for (int j = i + 1; j < nbNodes; j++)
                    tp.addLink(new Link(nodes[i], nodes[j], Link.Type.UNDIRECTED));
        } else {
            for (Node n : nodes) {
                n.setCommunicationRange(Math.max(getAbsoluteHeight(tp), getAbsoluteWidth(tp)));
            }
        }
    }
}
