import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class MainV1 {
	public static void main(String[] args) {
		Node.setModel("default", new RedGreenNodeV1());
		new JViewer(new Topology());
	}
}
