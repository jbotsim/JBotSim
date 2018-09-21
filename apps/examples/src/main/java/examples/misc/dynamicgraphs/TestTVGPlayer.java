package examples.misc.dynamicgraphs;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;
import jbotsimx.dygraph.TVG;
import jbotsimx.dygraph.TVGPlayer;

public class TestTVGPlayer {
	public static void main(String args[]){
		Topology tp=new Topology(400, 400);
        new JViewer(tp);

		TVG tvg=new TVG();
		tvg.buildFromFile("triangle.tvg");

        (new TVGPlayer(tvg, tp, 100)).start();
	}
}
