package examples.basic.helloworld;

import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class HelloWorld {
    public static void main(String[] args){
        Topology tp = new Topology();
        new JViewer(tp);
    }
}
