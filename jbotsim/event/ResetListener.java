package jbotsim.event;

/**
 * Created by acasteig on 3/17/15.
 */
public interface ResetListener {
    /**
     * Notifies that a reset was requested on this topology.
     */
    public void onReset();
}
