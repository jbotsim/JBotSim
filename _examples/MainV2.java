package _examples;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class MainV2 {
	public static void main(String[] args) {
		Node.setModel("default", new RedGreenNodeV2());
		new JViewer(new Topology());
	}
}
