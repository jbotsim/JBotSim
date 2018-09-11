package examples.fancy.canadairs;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.LinkResolver;
import jbotsimx.ui.JViewer;

import jbotsim.Color;

/**
 * Created by acasteig on 22/03/15.
 */
public class Main extends LinkResolver {
    static Topology topology;

    @Override
    public boolean isHeardBy(Node n1, Node n2) {
        if ((n1 instanceof Sensor && n2 instanceof Canadair) ||
                (n2 instanceof Sensor && n1 instanceof Canadair))
            return false;
        return (n1.isWirelessEnabled() && n2.isWirelessEnabled()
                && n1.distance(n2) < n1.getCommunicationRange());
    }
    public static void createMap(Topology topology){
        for (int i=0; i<6; i++)
            for (int j=0; j<4; j++)
                topology.addNode(i*100+180-(j%2)*30, j*100+100, new Sensor());
        topology.addNode(50, 400, new Station());
        for (Link link : topology.getLinks())
            link.setColor(Color.gray);
        topology.addNode(50, 500, new Canadair());
        topology.addNode(100, 500, new Canadair());
        topology.addNode(50, 50, new Lake());
    }
    public static void main(String[] args) {
        topology = new Topology(800,600);
        topology.setLinkResolver(new Main());
        topology.getMessageEngine().setSpeed(10);
        createMap(topology);
        topology.setClockSpeed(30);
        topology.setDefaultNodeModel(Fire.class);
        new JViewer(topology);
        topology.start();
    }
}
