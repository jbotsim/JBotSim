package examples.funny.cowboy;

import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

/**
 * Created by acasteig on 17/06/15.
 */
public class Farmer extends Node {
    public Farmer() {
        setIcon(Icons.FARMER);
        setIconSize(30);
        disableWireless();
    }
}
