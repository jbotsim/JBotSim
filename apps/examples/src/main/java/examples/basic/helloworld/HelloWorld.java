package examples.basic.helloworld;

import io.jbotsim.Topology;
import io.jbotsim.ui.JViewer;

public class HelloWorld {
    public static void main(String[] args){
        Topology tp = new Topology();
        new JViewer(tp);
    }
}
