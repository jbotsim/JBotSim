package io.jbotsim.dynamicity.graph;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

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
        Topology tp=new Topology();
        tp.setClockSpeed(100);
        new JViewer(tp);
        TVGPlayer player=new TVGPlayer(tvg, tp, 20);
        player.start();
    }
}
