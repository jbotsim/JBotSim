import jbotsim.core.Node;
import jbotsim.core.NodeAlgorithm;
import jbotsim.core.Topology; // for the main function
import jbotsim.gui.viewer.Viewer;// for the main function

public class BasicAlgorithm extends NodeAlgorithm{
    public void init(Node node) {
    }
    public void exit(Node node) {
    }
    public void performStep(Node node) {
        node.setColor((node.getNeighbors().size()==0)?"red":"green");
    }
    public static void main(String[] args) { // this function could be anywhere else
        Topology tp=new Topology();
        tp.getNodeModel("default").addAlgorithm(new BasicAlgorithm(), 10);
        new Viewer(tp);
    }
}
