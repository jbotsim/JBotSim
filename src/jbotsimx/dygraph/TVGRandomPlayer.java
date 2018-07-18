package jbotsimx.dygraph;

import java.util.Random;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class TVGRandomPlayer extends TVGPlayer{
    int timeBound;
    int presenceBound;
    Random rand=new Random();
    
    public TVGRandomPlayer(TVG tvg, Topology tp) {
        this(tvg, tp, 50);
    }
    public TVGRandomPlayer(TVG tvg, Topology tp, int timeBound) {
        this(tvg, tp, 50, 20);
    }
    public TVGRandomPlayer(TVG tvg, Topology tp, int timeBound, int presenceBound) {
        super(tvg, tp);
        this.timeBound=timeBound;
        this.presenceBound=presenceBound;
        for (TVLink l : super.tvg.tvlinks){
            l.setProperty("nextApp", rand.nextInt(timeBound-presenceBound));
            l.setProperty("nextDis", -1);
        }
        updateLinks();
    }
    protected void updateLinks(){
        int now=tp.getTime();
        for (TVLink l : super.tvg.tvlinks){
            int nextApp=(Integer)l.getProperty("nextApp");
            int nextDis=(Integer)l.getProperty("nextDis");
            if (now==nextApp){
                tp.addLink(l);
                l.setProperty("nextDis", now+(rand.nextInt(presenceBound-1))+1);
                l.setProperty("nextApp", now+(rand.nextInt(timeBound-presenceBound-1))+presenceBound+1);
            }else if(now==nextDis){
                tp.removeLink(l);
            }
        }
    }
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
