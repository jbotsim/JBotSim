package jbotsim.event;

/**
 * Created by acasteig on 3/17/15.
 */
public interface StartListener {
    /**
     * Notifies that a restart was requested on this topology.
     */
    void onStart();
}
