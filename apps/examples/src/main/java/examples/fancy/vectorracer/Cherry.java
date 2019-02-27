package examples.fancy.vectorracer;

import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

/**
 * Created by acasteig on 18/11/16.
 */
public class Cherry extends Node {
    @Override
    public void onStart() {
        disableWireless();
        setIcon(Icons.CHERRY);
    }
}
