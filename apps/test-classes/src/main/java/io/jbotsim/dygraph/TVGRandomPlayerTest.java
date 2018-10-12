package io.jbotsim.dygraph;

import io.jbotsim.Topology;
import io.jbotsim.dygraph.TVG;
import io.jbotsim.dygraph.TVGRandomPlayer;
import io.jbotsim.ui.JViewer;

public class TVGRandomPlayerTest {
    public static void main(String args[]){
        TVG tvg=new TVG();
        tvg.buildFromFile("/home/arnaud/workspace/code/jbotsim/_testing/dtn/star.tvg");
        Topology tp=new Topology();
        new JViewer(tp);
        tp.resetTime();
        tp.pause();
        tp.setClockSpeed(50);
        TVGRandomPlayer player = new TVGRandomPlayer(tvg, tp, 100);
        player.start();
        tp.resume();
    }
}
