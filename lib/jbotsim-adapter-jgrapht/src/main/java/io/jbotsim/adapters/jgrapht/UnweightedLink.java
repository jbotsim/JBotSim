package io.jbotsim.adapters.jgrapht;

import io.jbotsim.core.Link;
import org.jgrapht.graph.DefaultEdge;

/**
 * The {@link UnweightedLink} is a {@link DefaultEdge} which wraps a {@link Link}.
 */
public class UnweightedLink extends DefaultEdge {
    private Link link;

    public UnweightedLink(Link l) {
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
