package io.jbotsim.dygraph;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class EMEGPlayerTest {
    public static void main(String args[]){
        Topology tp=new Topology(400, 400);
        new JViewer(tp);
        TVG tvg=new TVG();
        tvg.buildCompleteGraph(30);
        (new EMEGPlayer(tvg, tp, .2, .4)).start();
        //new TopologyObserver(tp);
        tp.setClockSpeed(10);
    }
}
