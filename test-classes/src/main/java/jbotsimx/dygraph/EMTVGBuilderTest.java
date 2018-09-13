package jbotsimx.dygraph;

import jbotsim.Node;
import jbotsimx.dygraph.EMTVGBuilder;
import jbotsimx.dygraph.TVG;
import jbotsimx.dygraph.TVGPlayer;

import java.util.Vector;

public class EMTVGBuilderTest {
    public static void main(String args[]){
        Vector<Node> nodes=new Vector<Node>();
        Node n1=new Node(); n1.setLocation(100, 100); nodes.add(n1);
        Node n2=new Node(); n2.setLocation(100, 200); nodes.add(n2);
        Node n3=new Node(); n3.setLocation(200, 200); nodes.add(n3);
        Node n4=new Node(); n4.setLocation(200, 100); nodes.add(n4);
        TVG tvg= EMTVGBuilder.createGraph(nodes, 0.1, 0.1, 20);
        System.out.println(tvg);
        jbotsim.Topology tp=new jbotsim.Topology();
        tp.setClockSpeed(100);
        new jbotsimx.ui.JViewer(tp);
        TVGPlayer player=new TVGPlayer(tvg, tp, 20);
        player.start();
    }
}
