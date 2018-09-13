package jbotsimx.dygraph;

import java.util.Random;
import java.util.Vector;

import jbotsim.Node;

/*
 * Edge-Markovian Time-Varying Graph Builder.
 */
public class EMTVGBuilder {
    public static TVG createGraph(Vector<Node> nodes, double birthRate, double deathRate, int lifetime){
        TVG tvg=new TVG();
        double steadyProb = birthRate/(birthRate+deathRate);
        for (Node n : nodes)
            tvg.nodes.add(n);
        createInitialEdges(tvg, steadyProb);
        for (int date=1; date<lifetime-1; date++)
            createNextEdges(tvg, date, birthRate, deathRate);
        createLastEdges(tvg, lifetime-1);
        return tvg;
    }
    private static void createInitialEdges(TVG tvg, double steadyProb){
        Random r=new Random();
        Vector<Node> nodes=tvg.nodes;
        for (int i=0; i<nodes.size(); i++){
            for (int j=i+1; j<nodes.size(); j++){
                TVLink l=new TVLink(nodes.elementAt(i), nodes.elementAt(j));
                tvg.tvlinks.add(l);
                if (r.nextDouble()<steadyProb)
                    l.appearanceDates.add(0);
            }
        }        
    }
    private static void createNextEdges(TVG tvg, int date, double birthRate, double deathRate){
        Random r=new Random();
        for (TVLink l : tvg.tvlinks){
            if (l.isPresentAtTime(date-1)){
                if (r.nextDouble()<deathRate)
                    l.disappearanceDates.add(date);
            }else{
                if (r.nextDouble()<birthRate)
                    l.appearanceDates.add(date);
            }
        }
    }
    private static void createLastEdges(TVG tvg, int date){
        for (TVLink l : tvg.tvlinks)
            if (l.isPresentAtTime(date) && !l.isPresentAtTime(0))
                l.disappearanceDates.add(date);
    }
    public static void main(String args[]){
        Vector<Node> nodes=new Vector<Node>();
        Node n1=new Node(); n1.setLocation(100, 100); nodes.add(n1);
        Node n2=new Node(); n2.setLocation(100, 200); nodes.add(n2);
        Node n3=new Node(); n3.setLocation(200, 200); nodes.add(n3);
        Node n4=new Node(); n4.setLocation(200, 100); nodes.add(n4);
        TVG tvg=EMTVGBuilder.createGraph(nodes, 0.1, 0.1, 20);
        System.out.println(tvg);
        jbotsim.Topology tp=new jbotsim.Topology();
        tp.setClockSpeed(100);
        new jbotsimx.ui.JViewer(tp);
        TVGPlayer player=new TVGPlayer(tvg, tp, 20);
        player.start();
    }
}
