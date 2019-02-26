package examples.fancy.canadairs;

import io.jbotsim.core.Node;

/**
 * Created by acasteig on 22/03/15.
 */
public class Lake extends Node {

    public Lake(){
        disableWireless();
        setIcon("/io/jbotsim/ui/icons/lake.png");
        setIconSize(45);
    }
}
