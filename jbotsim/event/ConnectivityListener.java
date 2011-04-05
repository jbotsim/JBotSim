package jbotsim.event;

import jbotsim.Link;

public interface ConnectivityListener {
    /**
     * Notifies the underlying listener that a link has been added.
     * @param l The added link.
     */
    public void linkAdded(Link l);
    /**
     * Notifies the underlying listener that a link has been removed.
     * @param l The removed link.
     */
    public void linkRemoved(Link l);
}
