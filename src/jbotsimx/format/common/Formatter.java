package jbotsimx.format.common;

import jbotsim.Topology;

public interface Formatter {
    /**
     * Returns a string representation of this topology. The output of this
     * method can be subsequently used to reconstruct a topology with the
     * <tt>fromString</tt> method. Only the nodes and wired links are exported
     * here (not the topology's properties).
     */
    String exportTopology(Topology tp);

    /**
     * Imports nodes and wired links from the specified string representation of a
     * topology.
     *
     * @param s The string representation.
     */
    void importTopology(Topology tp, String s);
}
