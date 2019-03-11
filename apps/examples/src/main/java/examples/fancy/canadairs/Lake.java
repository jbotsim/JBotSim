package examples.fancy.canadairs;

import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

/**
 * Created by acasteig on 22/03/15.
 */
public class Lake extends Node {

    public Lake(){
        disableWireless();
        setIcon(Icons.LAKE);
        setIconSize(45);
    }
}
