package examples.fancy.vectorracer;

import jbotsim.Topology;

/**
 * Created by acasteig on 26/01/17.
 */
public class CherrySets {
    public static void distribute(Topology tp){
        for (int i=0; i<20; i++) {
            double x = Math.random() * 600 + 100;
            double y = Math.random() * 400 + 100;
            tp.addNode(x, y, new Cherry());
        }
    }
}
