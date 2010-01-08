import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class MainCentralized {
	public static void main(String[] args) {
		Topology tp=new Topology();
		tp.addTopologyListener(new RedGreenCentralized());
		new JViewer(tp);
	}
}
