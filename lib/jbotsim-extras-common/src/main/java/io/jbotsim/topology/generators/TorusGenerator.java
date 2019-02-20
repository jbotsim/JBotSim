package io.jbotsim.topology.generators;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

/**
 * <p>The {@link TorusGenerator} is a {@link TopologyGenerator} used to generated tori of {@link Node}s.</p>
 *
 * <p>This implementation relies on the {@link GridGenerator}.</p>
 */
public class TorusGenerator extends GridGenerator {

    /**
     * Creates a {@link TorusGenerator} creating a torus with nbRows rows and nbColumns columns.

     * @param nbRows the amount of desired rows.
     * @param nbColumns the amount of desired columns.
     */
    public TorusGenerator(int nbRows, int nbColumns) {
        super(nbRows, nbColumns);
    }

    @Override
    protected void addLinks(Topology tp, int nbRows, int nbColumns, Node[][] nodes) {
        super.addLinks(tp, nbRows, nbColumns, nodes);

        for (int i = 0; i < nbRows; i++)
            tp.addLink(new Link(nodes[i][0], nodes[i][nbColumns - 1]));

        for (int j = 0; j < nbColumns; j++)
            tp.addLink(new Link(nodes[0][j], nodes[nbRows - 1][j]));

    }
}
