package examples.misc.dynamicgraphs;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.gen.dynamic.graph.EMEGPlayer;
import io.jbotsim.gen.dynamic.graph.TVG;

public class EdgeMarkovian {
	public static void main(String args[]){
		Topology tp=new Topology();
		tp.setClockSpeed(100);
		new JViewer(tp);
		TVG tvg=new TVG(Node.class);
		tvg.buildCompleteGraph(10);
		(new EMEGPlayer(tvg, tp, .02, .6)).start();
	}
}
