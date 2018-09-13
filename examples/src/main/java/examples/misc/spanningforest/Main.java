package examples.misc.spanningforest;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;


public class Main {
	public static void main(String[] args){
		Topology tp = new Topology();
		new JViewer(tp);
		new Forest(tp);
		tp.start();
		tp.pause();
	}
}
