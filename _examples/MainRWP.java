package _examples;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class MainRWP {
	public static void main(String[] args) {
		Node.setModel("default", new RedGreenRWPNode());
		new JViewer(new Topology());
	}
}
