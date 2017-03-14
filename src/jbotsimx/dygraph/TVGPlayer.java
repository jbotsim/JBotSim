package jbotsimx.dygraph;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;

public class TVGPlayer implements ClockListener{
    protected TVG tvg;
    protected Topology tp;
    protected Integer period=null;
    /**
     * Plays the specified time-varying graph on the specified topology.
     * @param tvg the time-varying graph
     * @param tp the target topology
     */
    public TVGPlayer (TVG tvg, Topology tp){
        this(tvg, tp, null);
    }
    /**
     * Plays forever the specified time-varying graph on the specified topology
     * by taking all dates modulo the specified period.
     * @param tvg the time-varying graph
     * @param tp the target topology
     */
    public TVGPlayer (TVG tvg, Topology tp, Integer period){
        this.tvg=tvg;
        this.tp=tp;
        this.period=period;
        tp.setCommunicationRange(0);
        for (Node n : tvg.nodes)
            tp.addNode(n);
    }
    public void start(){
        tp.resetTime();
        tp.addClockListener(this);
        for (TVLink l : tvg.tvlinks)
            if (l.appearanceDates.contains(0))
                //tp.addLink(l, true); // add silently.. (nodes not notified)
                tp.addLink(l);
    }
    public void onClock(){
        updateLinks();
    }
    protected void updateLinks(){
        Integer time = (period==null) ? tp.getTime() : tp.getTime() % period;
        for (TVLink l : tvg.tvlinks){
            if (l.disappearanceDates.contains(time) && tp.getLinks().contains(l))
                tp.removeLink(l);
            if (l.appearanceDates.contains(time) && !tp.getLinks().contains(l))
                tp.addLink(l);
        }        
    }
}
