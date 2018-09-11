package examples.misc.dynamicgraphs;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.dygraph.EMEGPlayer;
import jbotsimx.dygraph.TVG;

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
