package _examples;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class MainV4 {
	public static void main(String[] args) {
		Node.setModel("default", new RedGreenNodeV4());
		new JViewer(new Topology());
	}
}
