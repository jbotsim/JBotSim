package examples.fancy.vectorracer;

import io.jbotsim.core.Node;

/**
 * Created by acasteig on 18/11/16.
 */
public class Cherry extends Node {
    @Override
    public void onStart() {
        disableWireless();
        setIcon("/examples/fancy/vectorracer/cherry.png");
    }
}
