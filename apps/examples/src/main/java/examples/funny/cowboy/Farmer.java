package examples.funny.cowboy;

import io.jbotsim.Node;

/**
 * Created by acasteig on 17/06/15.
 */
public class Farmer extends Node {
    public Farmer() {
        setIcon("/examples/funny/cowboy/farmer.png");
        setSize(30);
        disableWireless();
    }
}
