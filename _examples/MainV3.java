package _examples;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class MainV3 {
	public static void main(String[] args) {
		Node.setModel("default", new RedGreenNodeV3());
		new JViewer(new Topology());
	}
}
