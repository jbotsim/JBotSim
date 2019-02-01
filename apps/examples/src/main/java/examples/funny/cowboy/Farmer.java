package examples.funny.cowboy;

import io.jbotsim.core.Node;

/**
 * Created by acasteig on 17/06/15.
 */
public class Farmer extends Node {
    public Farmer() {
        setIcon("/io/jbotsim/ui/farmer.png");
        setSize(30);
        disableWireless();
    }
}
