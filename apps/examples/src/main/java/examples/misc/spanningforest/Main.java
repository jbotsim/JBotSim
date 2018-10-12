package examples.misc.spanningforest;

import io.jbotsim.Topology;
import io.jbotsim.ui.JViewer;


public class Main {
	public static void main(String[] args){
		Topology tp = new Topology();
		new JViewer(tp);
		new Forest(tp);
		tp.start();
		tp.pause();
	}
}
