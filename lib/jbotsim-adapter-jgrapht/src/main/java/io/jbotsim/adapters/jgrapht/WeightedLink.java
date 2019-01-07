package io.jbotsim.adapters.jgrapht;

import io.jbotsim.core.Link;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * The {@link WeightedLink} is a {@link DefaultWeightedEdge} which wraps a {@link Link}.
 */
public class WeightedLink extends DefaultWeightedEdge {
    private Link link;

    public WeightedLink(Link l) {
        super();
        link = l;
    }

    /**
     * The {@link Link} getter.
     * @return a {@link Link}
     */
    public Link getLink() {
        return link;
    }

    /**
     * The {@link Link} setter.
     * @param link the new {@link Link}.
     */
    public void setLink(Link link) {
        this.link = link;
    }
}
