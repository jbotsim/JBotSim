package examples.fancy.canadairs;

import jbotsim.Node;

/**
 * Created by acasteig on 22/03/15.
 */
public class Lake extends Node {

    public Lake(){
        disableWireless();
        setIcon("/examples/fancy/canadairs/lake.png");
        setSize(45);
    }
}
