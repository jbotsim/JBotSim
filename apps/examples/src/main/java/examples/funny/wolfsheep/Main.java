package examples.funny.wolfsheep;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;

/**
 * Created by acasteig on 31/08/16.
 */
public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology(800,600);
        tp.setClockSpeed(20);
        tp.disableWireless();
        tp.pause();
        for (int i = 0; i < 5; i++){
            // 3 randomly located wolves and sheeps.
            tp.addNode(-1, -1, new Wolf());
            tp.addNode(-1, -1, new Sheep());
        }
        new JViewer(tp);
    }
}
