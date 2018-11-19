package io.jbotsim.serialization;

import io.jbotsim.core.Topology;

/**
 * Objects implementing {@link TopologySerializer} are able to (de)serialize a {@link Topology} into a specific
 * {@link String} representation.
 */
public interface TopologySerializer {
    /**
     * Returns a string representation of this topology. The output of this
     * method can be subsequently used to reconstruct a topology with the
     * {@link TopologySerializer#importTopology(Topology, String)} method. Only the nodes and wired links are exported
     * here (not the topology's properties).
     *
     * @param tp The {@link Topology} object which must be exported
     * @return
     */
    String exportTopology(Topology tp);

    /**
     * Imports nodes and wired links from the specified string representation of a
     * topology.
     *
     * @param tp The {@link Topology} object which must be populated
     * @param s The {@link String} representation.
     */
    void importTopology(Topology tp, String s);
}
