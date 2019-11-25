package examples.basic.icons;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class MainIcons {
    public static void main(String[] args) {
        Topology topology = new Topology();

        int nbNodes = 10;
        deployAtCenter(topology, MovingNodeBlueOcean.class, nbNodes);
        deployAtCenter(topology, MovingNodeBlue.class, nbNodes);
        deployAtCenter(topology, MovingNodeFormer.class, nbNodes);
        deployAtCenter(topology, MovingNodePlus.class, nbNodes);
        deployAtCenter(topology, MovingNodeTransparent.class, nbNodes);
        deployAtCenter(topology, MovingNodeDefault.class, nbNodes);

        new JViewer(topology);
        topology.start();
    }

    private static void deployAtCenter(Topology tp, Class nodeClass, int nbNodes) {
        tp.setDefaultNodeModel(nodeClass);
        for(int i = 0; i< nbNodes; i++)
            tp.addNode(tp.getWidth()/2, tp.getHeight()/2);
    }
}
