package io.jbotsim.topology.generators;

import io.jbotsim.core.Topology;

/**
 * A {@link TopologyGenerator} is able to generate a specific structure in an existing {@link Topology} object
 * according to some specific characteristics.
 */
public interface TopologyGenerator {

    /**
     * Modifies the provided {@link Topology} according to the implementation.
     * @param topology the {@link Topology} to be modified by the generation
     */
    void generate(Topology topology);
}
